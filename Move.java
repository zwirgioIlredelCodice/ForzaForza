package connectx.ForzaForza;

import connectx.CXGameState;

public class Move implements Comparable {
    public Score s;
    public int move;
    private int player;
    private int M; // righe massime

    public Move(int move, Score s, int player, int M) {
        this.s = s;
        this.move = move;
        this.player = player;
        this.M = M;
    }

    /*
     * mette a confronto 2 mosse dal punto di vista del giocatore che deve
     * massimizzare
     */
    public int compareTo(Object o) {
        if ((o != null) && (o instanceof Move)) {
            Move nm = (Move) o;

            int compareScore = this.s.compareTo(nm.s);
            if (compareScore == 0) {
                int halfRow = (int) Math.floor(this.M / 2);

                int distanceA = Math.abs(this.move - halfRow);
                int distanceB = Math.abs(nm.move - halfRow);

                int cp = 0;
                if (distanceA < distanceB)
                    cp = 1;
                else if (distanceA > distanceB)
                    cp = -1;
                else
                    cp = 0;

                if (player == 0)
                    return cp;
                else
                    return -cp;
            } else {
                return compareScore;
            }
        }
        return -1;
    }
}
