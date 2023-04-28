package connectx.ForzaForza;

import connectx.CXPlayer;
import connectx.CXBoard;
import connectx.CXCell;

public class ForzaForza implements CXPlayer {

	private MoveEngine engine;

	/* Default empty constructor */
	public ForzaForza() {
	}

	public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
		engine = new MoveEngine(M, N, K, timeout_in_secs);
	}

	public int selectColumn(CXBoard B) {
		CXCell lasmove = B.getLastMove();
		if (lasmove != null) {
			engine.markColumn(lasmove.j);
		}
		int col = engine.IterativeDepening();
		engine.markColumn(col);
		return col;
	}

	public String playerName() {
		return "ForzaForza";
	}
}
