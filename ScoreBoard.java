package connectx.ForzaForza;
import java.util.LinkedList;


/**
 * questa classe si occupa di associare efficentemente un valore intero ad una
 * posizione di gioco in maniera euristica, ovvero di associare un numero 
 * che simboleggia le possibilità di vittoria per il giocatore 1 data da una posizione di gioco.
 * Il valore numerico più precisamente è calcolato come: il numero di X in linea effetuabili dal giocatore 1 
 * meno il numero di X in linea effetuabili dal giocatore 2 dando peso agli X in linea più completi.
 * 
 * esemio M=4 N=4 X=3 # pedin del giocatore 1
 *      . . . .                                                         . . . .
 *      . . . .                                                         . | . /
 *      . . . .                                                         . | / .
 *      . # . . da questa posizione è possibile fare 3 3 in fila        . # - -     il punteggio sarà +3
 * 
 * implementazione:
 * ogni cella della scacchiera è un insieme contenente tutti i modi per mettere X pedine in fila che
 * passano da quella cella. in questo modo per avere il punteggio dopo una nuova mossa il costo computazionale è O(X)
 * questa implementzione scambia memoria per velocità di calcolo 
 */
public class ScoreBoard {
    LinkedList<ScoreSet> sb[][];
    public int totalScore;

    int M,N,X;

    public ScoreBoard(int M, int N, int X) {
        this.M = M;
        this.N = N;
        this.X = X;

        sb = new LinkedList[M][N];

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                sb[i][j] = new LinkedList<ScoreSet>();
            }
        }
        initBoard();
    }

    /**
     * associa 
     * calcola tutti i possibili modi di mettere X pedine in fila e 
     * associa a ogni cella della scacchiera utti i possibili modi di 
     * mettere X pedine in fila che passano da essa
     * 
     * Costo O(MNX)
     */
    private void initBoard() {
        for (int i = 0; i < M; i++) { // row y
            for (int j = 0; j < N; j++) { // collum x
                
                if (M - i >= X) { // in giu
                    ScoreSet sc = new ScoreSet();
                    for (int k = i; k < X; k++) sb[k][j].add(sc);
                }
                if (N - j >= X) { // in avanti
                    ScoreSet sc = new ScoreSet();
                    for (int k = j; k < X; k++) sb[i][k].add(sc);
                }
                if ((M - i >= X) && (N - j >= X)) { // avanti giu
                    ScoreSet sc = new ScoreSet();
                    int kk = j;
                    for (int k = i; k < X; k++) {
                        sb[k][kk].add(sc);
                        kk++;
                    }
                }
                if ((i + 1 >= X) && (N - j >= X)) { // avanti su
                    ScoreSet sc = new ScoreSet();
                    int kk = j;
                    for (int k = i; i-k < X; k--) {
                        sb[k][kk].add(sc);
                        kk++;
                    }
                }
            }
        }
    }

    /**
     * aggiorna la struttura dopo una nuova mossa
     * @param m righa
     * @param n colonna
     * @param player giocatore che compie la mossa
     */
    public void move(int m, int n, int player) {
        LinkedList<ScoreSet> cell = sb[m][n];

        int deltaScore = 0;
        for (ScoreSet scoreSet : cell) {
            int ps = scoreSet.score;
            scoreSet.add(player);
            deltaScore += scoreSet.score - ps;
        }
        totalScore += deltaScore;
    }

    /**
     * aggiorna la struttura annullando una mossa
     * @param m righa
     * @param n colonna
     * @param player giocatore che compie la mossa
     */
    public void unmove(int m, int n, int player) {
        LinkedList<ScoreSet> cell = sb[m][n];

        int deltaScore = 0;
        for (ScoreSet scoreSet : cell) {
            int ps = scoreSet.score;
            scoreSet.sub(player);
            deltaScore += scoreSet.score - ps;
        }
        totalScore += deltaScore;
    }
}

/**
 * classe che simboleggia le X pedine in fila, tiene conto
 * del tipo e della quantità delle pedine in essa.
 */
class ScoreSet {
    public int score, p1, p2;

    public ScoreSet() {
        score = 0;
        p1 = 0;
        p2 = 0;
    }

    private void fixScore() {
        if (p1 == 0 || p2 == 0) { // se tutte le pedine sono dello stesso colore allora è valido
            if (p1 > p2) score = p1;
            else score = -p2;
        }
        else score = 0;
    }

    public void add(int player) {
        if (player == 0) p1++;
        else p2++;
        fixScore();
    }

    public void sub(int player) {
        if (player == 0) p1--;
        else p2--;
        fixScore();
    }
}
