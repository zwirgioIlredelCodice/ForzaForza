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

	private MyTimer timer;
	private MoveEngine engine;

	/* Default empty constructor */
	public ForzaForza() {
	}

	public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
		// New random seed for each game
		rand    = new Random(System.currentTimeMillis());
		myWin   = first ? CXGameState.WINP1 : CXGameState.WINP2;
		yourWin = first ? CXGameState.WINP2 : CXGameState.WINP1;
		timer = new MyTimer(timeout_in_secs);
		engine = new MoveEngine(Integer.MAX_VALUE, timer, true); // max Integer.MAX_VALUE
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
		timer.start();

		Integer[] L = B.getAvailableColumns();
		int save    = L[rand.nextInt(L.length)]; // Save a random column

		try {
			int col = engine.IterativeDepening(B);
			return col;
		} catch (TimeoutException e) {
			System.err.println("Timeout!!! Random column selected");
			return save;
		}
	}

    public String playerName() {
		return "ForzaForza";
	}
}


