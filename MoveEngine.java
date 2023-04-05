package connectx.ForzaForza;

import connectx.CXPlayer;
import connectx.CXBoard;
import connectx.CXGameState;
import connectx.CXCell;
import java.util.TreeSet;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

public class MoveEngine {
    private int max_depth;
    private boolean debug;

    private MyTimer timer;
    private Evaluation evaluator;

    private Score MAX_SCORE;
    private Score MIN_SCORE;


    public MoveEngine(int max_depth, MyTimer timer, boolean debug) {
        this.max_depth = max_depth;
        this.debug = debug;
        this.timer = timer;
        this.evaluator = new Evaluation();

        MAX_SCORE = new Score(Integer.MAX_VALUE, CXGameState.WINP1);
        MIN_SCORE = new Score(Integer.MIN_VALUE, CXGameState.WINP2);
    }

    public int IterativeDepening(CXBoard B) throws TimeoutException {
        Integer[] L = B.getAvailableColumns();
        Move[] ml = new Move[L.length];
        int move = 0;
        int player = B.currentPlayer();

        for (int i = 0; i < L.length; i++) { // inizializing move array
            ml[i] = new Move(L[i], new Score(0, CXGameState.OPEN), player, B.M);
        }

        int d = 1;
        for (d = 1; d <= max_depth; d++) {
            try {

                ml = movelist(B, ml, d);

                if (player == 0) {
                    Arrays.sort(ml, Collections.reverseOrder());
                } else {
                    Arrays.sort(ml);
                }

                move = ml[0].move;
                /*
                if (ml[0].s.state != CXGameState.OPEN && ml[0].s.state != CXGameState.DRAW) {
                    return move;
                }*/

                System.err.format("depth: %d, time: %d\n", d, timer.getTimeElapsed());
            } catch (TimeoutException e) {
                System.err.format("tempo finito\n");
                return move;
            }
        }
        return move;
    }

    private Move[] movelist(CXBoard B, Move[] prevMl, int depth) throws TimeoutException {
        int player = B.currentPlayer();
        Score eval;

        Score alpha = MIN_SCORE;
        Score beta = MAX_SCORE;

        if (depth <= 0) {
            return prevMl;
        }

        else if (player == 0) {
            eval = MIN_SCORE;
            for (int i = 0; i < prevMl.length; i++) {
                timer.checktime();

                if (prevMl[i].s.state == CXGameState.OPEN) {

                    B.markColumn(prevMl[i].move);

                    eval = max(eval, AlphaBeta(B, alpha, beta, depth - 1));
                    alpha = max(eval, alpha);

                    prevMl[i].s = eval; // update score value

                    B.unmarkColumn();
                }

                if (beta.compareTo(alpha) <= 0) {
                    break;
                }
            }

        } else {
            eval = MAX_SCORE;
            for (int i = 0; i < prevMl.length; i++) {
                timer.checktime();

                if (prevMl[i].s.state == CXGameState.OPEN) {
                    B.markColumn(prevMl[i].move);

                    eval = min(eval, AlphaBeta(B, alpha, beta, depth - 1));
                    beta = min(eval, beta);

                    prevMl[i].s = eval; // update score value

                    B.unmarkColumn();
                }

                if (beta.compareTo(alpha) <= 0) {
                    break;
                }
            }
        }
        return prevMl;
    }

	private Score AlphaBeta(CXBoard B, Score alpha, Score beta, int depth) throws TimeoutException {
        CXGameState state = B.gameState();
        int player = B.currentPlayer();
        Score eval;

        Integer[] L = B.getAvailableColumns();

        if (depth <= 0 || state != CXGameState.OPEN) {
            eval = evaluator.evaluate(B);
        }

        else if (player == 0) {
            eval = MIN_SCORE;
            for (int i : L) {
                timer.checktime();

                B.markColumn(i);

                eval = max(eval, AlphaBeta(B, alpha, beta, depth - 1));
                alpha = max(eval, alpha);

                B.unmarkColumn();

                if (beta.compareTo(alpha) <= 0) {
                    break;
                }
            }

        } else {
            eval = MAX_SCORE;
            for (int i : L) {
                timer.checktime();

                B.markColumn(i);

                eval = min(eval, AlphaBeta(B, alpha, beta, depth - 1));
                beta = min(eval, beta);

                B.unmarkColumn();

                if (beta.compareTo(alpha) <= 0) {
                    break;
                }
            }
        }
        return eval;
	}

	private Score max(Score a, Score b) {
        if (a.compareTo(b) >= 0) return a;
        else return b;
	}

	private Score min(Score a, Score b) {
        if (a.compareTo(b) <= 0) return a;
        else return b;
	}
}
