package connectx.ForzaForza;

import connectx.CXPlayer;
import connectx.CXBoard;
import connectx.CXGameState;
import connectx.CXCell;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class ForzaForza implements CXPlayer {
	private CXGameState myWin;
	private CXGameState yourWin;

	private MyTimer timer;
	private MoveEngine engine;

	/* Default empty constructor */
	public ForzaForza() {
	}

	public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
		timer = new MyTimer(timeout_in_secs);
		engine = new MoveEngine(timer);
	}

	public int selectColumn(CXBoard B) {
		timer.start();

		int col = engine.IterativeDepening(B);
		return col;
	}

	public String playerName() {
		return "ForzaForza";
	}
}
