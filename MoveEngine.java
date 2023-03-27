package connectx.ForzaForza;

import connectx.CXPlayer;
import connectx.CXBoard;
import connectx.CXGameState;
import connectx.CXCell;
import java.util.TreeSet;
import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class MoveEngine {
    private int max_depth;
    private boolean debug;

    private MyTimer timer;



    public MoveEngine(int max_depth, MyTimer timer, boolean debug) {
        this.max_depth = max_depth;
        this.debug = debug;
        this.timer = timer;
    }

    public int IterativeDepening(CXBoard B) throws TimeoutException {
        int move = 0;
        int d = 1;
        for (d = 1; d <= max_depth; d++) {
            try {
                move = bestmove(B, d);
            } catch (TimeoutException e) {
                return move;
            }
        }
        System.err.format("### Depth %d\n", d);
        return move;
    }

    private int bestmove(CXBoard B, int depth) throws TimeoutException {
        Integer[] L = B.getAvailableColumns();
        int bestmove = L[0];
        int bestval;

        int player = B.currentPlayer();

        System.err.format("player %d\n", player);


        if (player == 0) {
            bestval = Integer.MIN_VALUE;
            for (int i : L) {
                timer.checktime();

                B.markColumn(i);
                int val = AlphaBeta(B, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1);
                if (val > bestval) {
                    bestval = val;
                    bestmove = i;
                }
                System.err.format("move = %d, value = %d\n", i, val);
                B.unmarkColumn();
            }
        } else {
            bestval = Integer.MAX_VALUE;
            for (int i : L) {
                timer.checktime();

                B.markColumn(i);
                int val = AlphaBeta(B, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1);
                if (val < bestval) {
                    bestval = val;
                    bestmove = i;
                }
                System.err.format("move = %d, value = %d\n", i, val);
                B.unmarkColumn();
            }
        }

        System.err.format("CHOSEN move = %d, value = %d\n", bestmove, bestval);
        return bestmove;
	}

	private int AlphaBeta(CXBoard B, int alpha, int beta, int depth) throws TimeoutException {
        CXGameState state = B.gameState();
        int player = B.currentPlayer();
        int eval;

        Integer[] L = B.getAvailableColumns();

        if (depth <= 0 || state != CXGameState.OPEN) {
            eval = evaluate(B);
        }

        else if (player == 0) {
            eval = Integer.MIN_VALUE;
            for (int i : L) {
                timer.checktime();

                B.markColumn(i);

                eval = Math.max(eval, AlphaBeta(B, alpha, beta, depth - 1));
                alpha = Math.max(eval, alpha);

                B.unmarkColumn();

                if (beta <= alpha) {
                    break;
                }
            }

        } else {
            eval = Integer.MAX_VALUE;
            for (int i : L) {
                timer.checktime();

                B.markColumn(i);

                eval = Math.min(eval, AlphaBeta(B, alpha, beta, depth - 1));
                beta = Math.min(eval, beta);

                B.unmarkColumn();

                if (beta <= alpha) {
                    break;
                }
            }
        }
        return eval;
	}

	private int evaluate(CXBoard B) {
        CXGameState state = B.gameState();
        int value = 0;

        switch (state) {
            case OPEN:
                value = 0;
                break;
            case DRAW:
                value = 0;
                break;
            case WINP1:
                value = Integer.MAX_VALUE;
                break;
            case WINP2:
                value = Integer.MIN_VALUE;
                break;
            default:
                break;
        }
        return value;
	}
}
