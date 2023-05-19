package connectx.ForzaForza;

import connectx.CXPlayer;
import connectx.CXBoard;
import connectx.CXCell;

import java.util.Scanner;

public class ForzaForza implements CXPlayer {

	private MoveEngine engine;

	/* Default empty constructor */
	public ForzaForza() {
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		ForzaForza engine = new ForzaForza();

		boolean exit = false;

		while (!exit) {
			System.out.println("============\n1. test\n2. extreme test\n0. exit\n============");
			int choice = s.nextInt();
			switch (choice) {
				case 0:
					exit = true;
					break;
			
				case 1:
					engine.perftest();
					break;
				
				case 2:
					engine.perftest(100, 100, 30);
					break;
			
				default:
					System.out.println("not a valid choice :(");
					break;
			}
		}
		s.close();
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

	public void perftest() {
		engine = new MoveEngine(6, 7, 4, 10);

		engine.IterativeDepening();
	}

	public void perftest(int M, int N, int X) {
		engine = new MoveEngine(M, N, X, 10);

		engine.IterativeDepening();
	}
}
