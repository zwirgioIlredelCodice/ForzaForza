package connectx.ForzaForza;

import connectx.CXBoard;
import connectx.CXGameState;
import connectx.CXCell;
import connectx.CXCellState;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

public class MoveEngine extends CXBoard {

    private MyTimer timer;

    private BitBoard bitBoard;
    private CacheTable table;

    private Score MAX_SCORE;
    private Score MIN_SCORE;

    final boolean debug = true;

    private int perft;
    private int cutoff;
    private int hit;

    public MoveEngine(int M, int N, int X, int timeout_in_secs) {
        super(M, N, X);

        this.timer = new MyTimer(timeout_in_secs);
        bitBoard = new BitBoard(M, N);
        table = new CacheTable();

        MAX_SCORE = new Score(Integer.MAX_VALUE, CXGameState.WINP1);
        MIN_SCORE = new Score(Integer.MIN_VALUE, CXGameState.WINP2);

        perft = 0;
        cutoff = 0;
        hit = 0;
    }

    @Override
    public CXGameState markColumn(int col) throws IndexOutOfBoundsException, IllegalStateException {
        int pl = currentPlayer;
        CXGameState ret = super.markColumn(col);

        CXCell lastmove = getLastMove();
        bitBoard.markBit(lastmove.i, lastmove.j, pl);

        return ret;
    }

    @Override
    public void unmarkColumn() throws IllegalStateException {
        
        CXCell lastmove = getLastMove();
        super.unmarkColumn();
        bitBoard.markBit(lastmove.i, lastmove.j, currentPlayer);
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
            ml[i] = new Move(L[i], new Score(0, CXGameState.OPEN), currentPlayer, M, 0, 0, 0, 0);
        }

        int d = 1;
        for (d = 1; d <= max_depth; d++) {
            try {
                table.reset();

                ml = movelist(ml, d);

                if (currentPlayer == 0) {
                    Arrays.sort(ml, Collections.reverseOrder());
                } else {
                    Arrays.sort(ml);
                }

                move = ml[0].move;

                System.err.format("depth: %d, time: %d\n", d, timer.getTimeElapsed());
            } catch (TimeoutException e) {
                System.err.format("time finished\n");

                // for making the board in sync in case of TimeoutException
                while(MC.size() > sizeMC) {
                    unmarkColumn();
                }
                
                if (debug) {
                    for (Move m : ml) {
                        System.err.println(m);
                    }
                }

                return move;
            }
        }

        if (debug) {
            for (Move m : ml) {
                System.err.println(m);
            }
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

                    perft = 0;
                    cutoff = 0;
                    hit = 0;

                    markColumn(prevMl[i].move);

                    eval = max(eval, AlphaBeta(alpha, beta, depth - 1));
                    alpha = max(eval, alpha);

                    prevMl[i].s = eval; // update score value
                    prevMl[i].depth = depth;
                    prevMl[i].nodes = perft;
                    prevMl[i].cutoff = cutoff;
                    prevMl[i].hit = hit;

                    unmarkColumn();
                }
            }

        } else {
            eval = MAX_SCORE;
            for (int i = 0; i < prevMl.length; i++) {
                timer.checktime();

                if (prevMl[i].s.state == CXGameState.OPEN) {

                    perft = 0;
                    cutoff = 0;
                    hit = 0;

                    markColumn(prevMl[i].move);

                    eval = min(eval, AlphaBeta(alpha, beta, depth - 1));
                    beta = min(eval, beta);

                    prevMl[i].s = eval; // update score value
                    prevMl[i].depth = depth;
                    prevMl[i].nodes = perft;
                    prevMl[i].cutoff = cutoff;
                    prevMl[i].hit = hit;

                    unmarkColumn();
                }
            }
        }
        return prevMl;
    }

    private Score AlphaBeta(Score alpha, Score beta, int depth) throws TimeoutException {
        
        Score eval;
        eval = table.get(bitBoard.getKey());
        if (eval != null) {
            hit++;
            return eval;
        }

        Integer[] L = getAvailableColumns();

        if (depth <= 0 || gameState != CXGameState.OPEN) {
            eval = evaluate();
            perft++;
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
                    cutoff++;
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
                    cutoff++;
                    break;
                }
            }
        }
        table.put(bitBoard.getKey(), eval);

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

        for (int j = 0; j < N; j++) {
            timer.checktime();
            int i = RP[j] + 1;
            if (i <  M) {
                value += possibleScore(i, j);
            }
        }
        return value;
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
        timer.checktime();
        for (int k = 1; j - k >= 0 && B[i][j - k] != notPlayerCell; k++) {
            if (B[i][j - k] == s)
                alreadyFilled++;
            n++;
        } // boardackward check
        timer.checktime();
        for (int k = 1; j + k < N && B[i][j + k] != notPlayerCell; k++) {
            if (B[i][j + k] == s)
                alreadyFilled++;
            n++;
        } // forward check
        if (n >= X)
            score += alreadyFilled;

        // Vertical check
        n = 1;
        alreadyFilled = 1;
        timer.checktime();
        for (int k = 1; i + k < M && B[i + k][j] != notPlayerCell; k++) {
            if (B[i + k][j] == s)
                alreadyFilled++;
            n++;
        }
        if (n >= X)
            score += alreadyFilled;

        // Diagonal check
        n = 1;
        alreadyFilled = 1;
        timer.checktime();
        for (int k = 1; i - k >= 0 && j - k >= 0 && B[i - k][j - k] != notPlayerCell; k++) {
            if (B[i - k][j - k] == s)
                alreadyFilled++;
            n++;
        } // boardackward check
        timer.checktime();
        for (int k = 1; i + k < M && j + k < N && B[i + k][j + k] != notPlayerCell; k++) {
            if (B[i + k][j + k] == s)
                alreadyFilled++;
            n++;
        } // forward check
        if (n >= X)
            score += alreadyFilled;

        // Anti-diagonal check
        n = 1;
        alreadyFilled = 1;
        timer.checktime();
        for (int k = 1; i - k >= 0 && j + k < N && B[i - k][j + k] != notPlayerCell; k++) {
            if (B[i - k][j + k] == s)
                alreadyFilled++;
            n++;
        } // boardackward check
        timer.checktime();
        for (int k = 1; i + k < M && j - k >= 0 && B[i + k][j - k] != notPlayerCell; k++) {
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
