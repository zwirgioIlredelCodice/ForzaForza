package connectx.ForzaForza;

import java.math.BigInteger;

public class BitBoard {

    BigInteger p1;
    BigInteger all;

    int M;
    int N;

    public BitBoard(int M, int N) {
        p1 = BigInteger.ZERO; // 1 se c'è la pedina di p1 0 altrimenti
        all = BigInteger.ZERO; // p1 and p2

        this.M = M;
        this.N = N;
    }

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

    // https://en.wikipedia.org/wiki/Pairing_function
    // ritorna un numero che rappresenta univocamente la scacchiera
    public BigInteger getKey() {
        // elegant pairing function by Matthew Szudzik @ Wolfram Research, Inc.
        // function elegantPair(x, y) {
        //      return (x >= y) ? (x * x + x + y) : (y * y + x);
        // }
        // per rappresentarlo basta usare p1 e all perche in all c'e p2
        // si sa per per certo che all >= p1 perche all = p1 and p2
        BigInteger key = all.pow(2);
        key = key.add(p1);
        return key;
    }
}
