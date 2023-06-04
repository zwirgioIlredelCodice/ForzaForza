package connectx.ForzaForza;
import java.math.BigInteger;


/**
 * Questa classe rappresenta la scacchiera di gioco utilizzando il dato
 * BigInteger, questo viene fatto pe identificare facilmente uno 
 * stato del gioco utilizzando solo un numero, ovvero rappresenta 
 * uno stato del gioco con una chiave.
 */

public class BitBoardBigInteger implements BitBoard<BigInteger> {

    /**
     * Biginteger che si occupa di codificare la posizione di tutte le
     * pedine del giocatore 1.
     * 
     * la codifica consiste nel:
     * * settare a 1 il bit in posizione row * M + col se nella cella row,col c'è una pedina del giocatore 1
     * * settare il bit a 0 altrimenti
     */
    BigInteger p1;
    /**
     * Biginteger che si occupa di codificare la posizione di tutte le
     * pedine del giocatore 1 e 2.
     * 
     * la codifica consiste nel:
     * * settare a 1 il bit in posizione row * M + col se nella cella row,col c'è una pedina del giocatore 1 o 2
     * * settare il bit a 0 altrimenti
     */
    BigInteger all;

    int M;
    int N;

    public BitBoardBigInteger(int M, int N) {
        p1 = BigInteger.ZERO; // 1 se c'è la pedina di p1 0 altrimenti
        all = BigInteger.ZERO; // p1 and p2

        this.M = M;
        this.N = N;
    }

    /**
     * metodo che esegue una mossa effetuata sulla scacchiera
     * @param row la righa dove viene messa la pedina
     * @param col la colonna dove viene messa la pedina
     * @param player in giocatore che fa la mossa
     */
    public void markBit(int row, int col, int player) {
        int n = row * M + col;
        if (player == 0) {
            p1 = p1.flipBit(n);
        }
        all = all.flipBit(n);
    }

    public void unmarkBit(int row, int col, int player) {
        markBit(row, col, player);
    }


    /**
     * questo metodo codifica p1 e all in un unico numero utilizzando l'idea di 
     * funzione coppia https://en.wikipedia.org/wiki/Pairing_function
     * @return un numero che rappresenta univocamente la scacchiera di gioco
     */
    public BigInteger getKey() {
        // implementazione della funzione coppia elegant pairing function by Matthew Szudzik @ Wolfram Research, Inc.
        // function elegantPair(x, y) {
        //      return (x >= y) ? (x * x + x + y) : (y * y + x);
        // }
        // si sa per per certo che all >= p1 perche all = p1 and p2
        BigInteger key = all.pow(2);
        key = key.add(p1);
        return key;
    }
}
