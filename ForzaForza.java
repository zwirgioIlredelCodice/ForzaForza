package connectx.ForzaForza;

import connectx.CXPlayer;
import connectx.CXBoard;
import connectx.CXCell;

import java.util.Scanner;

/** 
 * ForzaForza è la classe principale del progetto ed è 
 * la parte che si occupa di dialogare con gli altri giocatori
*/

public class ForzaForza implements CXPlayer {

	/**
	 * la classe engine è la classe che si occupa
	 * di calcolare la mossa migliore
	 */
	private MoveEngine engine;

	/* Default empty constructor */
	public ForzaForza() {
	}

	/**
	 * Questo metodo è stata creata per testare e confrontare
	 * le performance del giocatore
	 */
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

	/**
	 * inizzializza il giocatore software
	 */
	public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
		
		String s = String.join(
			System.getProperty("line.separator"),
			"____ ____ ____ ___  ____ ____ ____ ____ ___  ____",
			"|___ |  | |__/   /  |__| |___ |  | |__/   /  |__|",
			"|    |__| |  \\  /__ |  | |    |__| |  \\  /__ |  |",
			" ",
			"by Fabio Murer"
		);
		System.err.println(s);
		engine = new MoveEngine(M, N, K, timeout_in_secs);
	}

	/*
	 * questo metodo deve effettuare una scelta ottimale
	 * della mossa tra tutte quelle possibili
	 * Costo: O(B.M^B.N*(B.M*B.N)^2)
	 */
	public int selectColumn(CXBoard B) {
		/*
		 * questa parte di codice si occupa di tenere sincronizzata
		 * lo stato del gioco tra l'oggetto engine e il gioco corrente
		 */
		CXCell lasmove = B.getLastMove();
		if (lasmove != null) {
			engine.markColumn(lasmove.j);
		}
		int col = engine.IterativeDepening(); // ritorna la mossa migiore
		engine.markColumn(col);
		return col;
	}

	public String playerName() {
		return "ForzaForza";
	}

	// metodi per valutare le performance del giocatore
	public void perftest() {
		engine = new MoveEngine(6, 7, 4, 10);

		engine.IterativeDepening();
	}

	public void perftest(int M, int N, int X) {
		engine = new MoveEngine(M, N, X, 10);

		engine.IterativeDepening();
	}
}
