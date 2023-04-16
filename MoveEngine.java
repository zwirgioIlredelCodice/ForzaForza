package connectx.ForzaForza;

import connectx.CXPlayer;
import connectx.CXBoard;
import connectx.CXGameState;
import connectx.CXCell;
import connectx.CXCellState;
import java.util.TreeSet;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

public class MoveEngine extends CXBoard {

    private MyTimer timer;

    private Score MAX_SCORE;
    private Score MIN_SCORE;

    public MoveEngine(int M, int N, int X, int timeout_in_secs) {
        super(M, N, X);
        this.timer = new MyTimer(timeout_in_secs);
        MAX_SCORE = new Score(Integer.MAX_VALUE, CXGameState.WINP1);
        MIN_SCORE = new Score(Integer.MIN_VALUE, CXGameState.WINP2);
    }

    public void updateBoard(CXCell move) {
        if (move != null) {
            markColumn(move.j);
        }
    }

    public int IterativeDepening() {
        timer.start();

        // for making the board in sync in case of TimeoutException
        int sizeMC = MC.size();

        Integer[] L = getAvailableColumns();
        Move[] ml = new Move[L.length];
        int move = 0;

        int max_depth = numOfFreeCells();

        for (int i = 0; i < L.length; i++) { // inizializing move array
            ml[i] = new Move(L[i], new Score(0, CXGameState.OPEN), currentPlayer, M);
        }

        int d = 1;
        for (d = 1; d <= max_depth; d++) {
            try {

                ml = movelist(ml, d);

                if (currentPlayer == 0) {
                    Arrays.sort(ml, Collections.reverseOrder());
                } else {
                    Arrays.sort(ml);
                }

                move = ml[0].move;

                System.err.format("depth: %d, time: %d\n", d, timer.getTimeElapsed());
            } catch (TimeoutException e) {
                System.err.format("tempo finito\n");

                // for making the board in sync in case of TimeoutException
                while(MC.size() > sizeMC) {
                    unmarkColumn();
                }
                for (Move m : ml) {
                    System.err.println(m);
                }

                return move;
            }
        }

        for (Move m : ml) {
            System.err.println(m);
        }

        return move;
    }

    private Move[] movelist(Move[] prevMl, int depth) throws TimeoutException {
        Score eval;

        Score alpha = MIN_SCORE;
        Score beta = MAX_SCORE;

        if (depth <= 0) {
            return prevMl;
        }

        else if (currentPlayer == 0) {
            eval = MIN_SCORE;
            for (int i = 0; i < prevMl.length; i++) {
                timer.checktime();

                if (prevMl[i].s.state == CXGameState.OPEN) {

                    markColumn(prevMl[i].move);

                    eval = max(eval, AlphaBeta(alpha, beta, depth - 1));
                    alpha = max(eval, alpha);

                    prevMl[i].s = eval; // update score value

                    unmarkColumn();
                }
            }

        } else {
            eval = MAX_SCORE;
            for (int i = 0; i < prevMl.length; i++) {
                timer.checktime();

                if (prevMl[i].s.state == CXGameState.OPEN) {
                    markColumn(prevMl[i].move);

                    eval = min(eval, AlphaBeta(alpha, beta, depth - 1));
                    beta = min(eval, beta);

                    prevMl[i].s = eval; // update score value

                    unmarkColumn();
                }
            }
        }
        return prevMl;
    }

    private Score AlphaBeta(Score alpha, Score beta, int depth) throws TimeoutException {
        Score eval;

        Integer[] L = getAvailableColumns();

        if (depth <= 0 || gameState != CXGameState.OPEN) {
            eval = evaluate();
        }

        else if (currentPlayer == 0) {
            eval = MIN_SCORE;
            for (int i : L) {
                timer.checktime();

                markColumn(i);

                eval = max(eval, AlphaBeta(alpha, beta, depth - 1));
                alpha = max(eval, alpha);

                unmarkColumn();

                if (beta.compareTo(alpha) <= 0) {
                    break;
                }
            }

        } else {
            eval = MAX_SCORE;
            for (int i : L) {
                timer.checktime();

                markColumn(i);

                eval = min(eval, AlphaBeta(alpha, beta, depth - 1));
                beta = min(eval, beta);

                unmarkColumn();

                if (beta.compareTo(alpha) <= 0) {
                    break;
                }
            }
        }
        return eval;
    }

    private Score max(Score a, Score b) {
        if (a.compareTo(b) >= 0)
            return a;
        else
            return b;
    }

    private Score min(Score a, Score b) {
        if (a.compareTo(b) <= 0)
            return a;
        else
            return b;
    }

    public Score evaluate() throws TimeoutException {
        int value = evaluateBoard();
        Score s = new Score(value, gameState);
        return s;
    }

    private int evaluateBoard() throws TimeoutException {
        int value = 0;

        for (int i = 0; i < M; i++) {
            timer.checktime();
            int j = getFirstPlayerCell(i);
            if (j != -1) {
                value += possibleScore(i, j);
            }
        }
        return value;
    }

    private int getFirstPlayerCell(int colum) throws TimeoutException {
        int i = 0;
        for (i = 0; i < N; i++) {
            timer.checktime();
            if (B[colum][i] != CXCellState.FREE)
                return i;
        }
        return -1;
    }

    private int possibleScore(int i, int j) throws TimeoutException {
        CXCellState notPlayerCell = currentPlayer == 0 ? CXCellState.P2 : CXCellState.P1;
        CXCellState s = B[i][j];
        int n;
        int alreadyFilled;

        int score = 0;

        // Useless pedantic check
        if (s == CXCellState.FREE)
            return 0;

        // Horizontal check
        n = 1;
        alreadyFilled = 1;
        for (int k = 1; j - k >= 0 && B[i][j - k] != notPlayerCell; k++) {
            timer.checktime();
            if (B[i][j - k] == s)
                alreadyFilled++;
            n++;
        } // boardackward check
        for (int k = 1; j + k < N && B[i][j + k] != notPlayerCell; k++) {
            timer.checktime();
            if (B[i][j + k] == s)
                alreadyFilled++;
            n++;
        } // forward check
        if (n >= X)
            score += alreadyFilled;

        // Vertical check
        n = 1;
        alreadyFilled = 1;
        for (int k = 1; i + k < M && B[i + k][j] != notPlayerCell; k++) {
            timer.checktime();
            if (B[i + k][j] == s)
                alreadyFilled++;
            n++;
        }
        if (n >= X)
            score += alreadyFilled;

        // Diagonal check
        n = 1;
        alreadyFilled = 1;
        for (int k = 1; i - k >= 0 && j - k >= 0 && B[i - k][j - k] != notPlayerCell; k++) {
            timer.checktime();
            if (B[i - k][j - k] == s)
                alreadyFilled++;
            n++;
        } // boardackward check
        for (int k = 1; i + k < M && j + k < N && B[i + k][j + k] != notPlayerCell; k++) {
            timer.checktime();
            if (B[i + k][j + k] == s)
                alreadyFilled++;
            n++;
        } // forward check
        if (n >= X)
            score += alreadyFilled;

        // Anti-diagonal check
        n = 1;
        alreadyFilled = 1;
        for (int k = 1; i - k >= 0 && j + k < N && B[i - k][j + k] != notPlayerCell; k++) {
            timer.checktime();
            if (B[i - k][j + k] == s)
                alreadyFilled++;
            n++;
        } // boardackward check
        for (int k = 1; i + k < M && j - k >= 0 && B[i + k][j - k] != notPlayerCell; k++) {
            timer.checktime();
            if (B[i + k][j - k] == s)
                alreadyFilled++;
            n++;
        } // forward check
        if (n >= X)
            score += alreadyFilled;

        if (s == CXCellState.P2)
            score = -score;
        return score;
    }
}
