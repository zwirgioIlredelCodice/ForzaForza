package connectx.ForzaForza;

import connectx.CXPlayer;
import connectx.CXBoard;
import connectx.CXGameState;
import connectx.CXCell;
import java.util.TreeSet;
import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;


public class ForzaForza implements CXPlayer {
	private Random rand;
	private CXGameState myWin;
	private CXGameState yourWin;
	private int  TIMEOUT;
	private long START;

	/* Default empty constructor */
	public ForzaForza() {
	}

	public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
		// New random seed for each game
		rand    = new Random(System.currentTimeMillis());
		myWin   = first ? CXGameState.WINP1 : CXGameState.WINP2;
		yourWin = first ? CXGameState.WINP2 : CXGameState.WINP1;
		TIMEOUT = timeout_in_secs;
	}

	/**
	 * Selects a free colum on game board.
	 * <p>
	 * Selects a winning column (if any), otherwise selects a column (if any)
	 * that prevents the adversary to win with his next move. If both previous
	 * cases do not apply, selects a random column.
	 * </p>
	 */
	public int selectColumn(CXBoard B) {
		START = System.currentTimeMillis(); // Save starting time

		Integer[] L = B.getAvailableColumns();
		int save    = L[rand.nextInt(L.length)]; // Save a random column

		try {
			int col = bestmove(B, L, 2);
			return col;
		} catch (TimeoutException e) {
			System.err.println("Timeout!!! Random column selected");
			return save;
		}
	}

	private void checktime() throws TimeoutException {
		if ((System.currentTimeMillis() - START) / 1000.0 >= TIMEOUT * (99.0 / 100.0))
			throw new TimeoutException();
	}


	private int bestmove(CXBoard B, Integer[] L, int depth) throws TimeoutException {
        int bestmove = 0;
        int bestval;

        int player = B.currentPlayer();

        System.err.format("player %d\n", player);


        bestval = -1;
        for (int i : L) {
            B.markColumn(i);

            int val = AlphaBeta(B, -1, +1, depth);
            if (val > bestval) {
                bestval = val;
                bestmove = i;
            }
            System.err.format("move = %d, value = %d\n", i, val);
            B.unmarkColumn();
        }

        System.err.format("CHOSEN move = %d, value = %d\n", bestmove, bestval);
        return bestmove;
	}

	private int evaluate(CXBoard B) {
        CXGameState state = B.gameState();
        int value = 0;

        if (state == myWin) {
            value = 1;
        }
        else if (state == yourWin) {
            value = -1;
        } else {
            value = 0;
        }
        return value;
	}

	private int AlphaBeta(CXBoard B, int alpha, int beta, int depth) {
        CXGameState state = B.gameState();
        int player = B.currentPlayer();
        int eval;

        if (depth == 0 || state != CXGameState.OPEN) {
            eval = evaluate(B);
        }

        else if (player == 0) {
            eval = -1;
            Integer[] L = B.getAvailableColumns();
            for (int i : L) {
                B.markColumn(i);

                eval = Math.max(eval, AlphaBeta(B, alpha, beta, depth - 1));
                alpha = Math.max(eval, alpha);

                if (beta <= alpha) {
                    break;
                }

                B.unmarkColumn();
            }

        } else {

            eval = 1;
            Integer[] L = B.getAvailableColumns();
            for (int i : L) {
                B.markColumn(i);

                eval = Math.min(eval, AlphaBeta(B, alpha, beta, depth - 1));
                alpha = Math.min(eval, alpha);

                if (beta <= alpha) {
                    break;
                }

                B.unmarkColumn();
            }
        }
        return eval;
	}



	public String playerName() {
		return "ForzaForza";
	}
}


