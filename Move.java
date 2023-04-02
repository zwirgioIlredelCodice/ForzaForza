package connectx.ForzaForza;

import connectx.CXGameState;

public class Move implements Comparable {
    private Score s;
    public int move;
    private int M; // righe massime

    public Move(int move, Score s, int M) {
        this.s = s;
        this.move = move;
        this.M = M;
    }

    /*
    mette a confronto 2 mosse dal punto di vista del giocatore che deve massimizzare
    */
    public int compareTo(Object o) {
        if ((o != null) && (o instanceof Move)) {
            Move nm = (Move) o;

            int compareScore = this.s.compareTo(nm.s);
            if (compareScore == 0) {
                int halfRow = (int) Math.floor(this.M / 2);

                int distanceA = Math.abs(this.move - halfRow);
                int distanceB = Math.abs(nm.move   - halfRow);

                if (distanceA < distanceB) return 1;
                else if (distanceA > distanceB) return -1;
                else return 0;
            } else {
                return compareScore;
            }
        }
        return -1;
    }
}
