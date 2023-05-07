package connectx.ForzaForza;

public class Move implements Comparable<Move> {
    public Score s;
    public int move;
    private int player;
    private int M; // righe massime

    public int depth;
    public int nodes;
    public int cutoff;
    public int hit;

    public Move(int move, Score s, int player, int M, int depth, int nodes, int cutoff, int hit) {
        this.s = s;
        this.move = move;
        this.player = player;
        this.M = M;
        this.depth = depth;
        this.nodes = nodes;
        this.cutoff = cutoff;
        this.hit = hit;
    }


    private int compareDepth(int d1, int d2) {
        if (d1 == d2) return 0;
        else if (d1 < d2) return 1;
        else return -1;
    }
    /*
     * mette a confronto 2 mosse dal punto di vista del giocatore che deve
     * massimizzare
     */
    public int compareTo(Move m) {

        int compareScore = this.s.compareTo(m.s);
        int compareD = compareDepth(this.depth, m.depth);
        if (compareScore == 0) {
            switch (this.s.state) {
                case OPEN:
                    break;
                case DRAW:
                    compareScore = -compareD;
                    break;
                case WINP1:
                    compareScore = compareD;
                    break;
                case WINP2:
                    compareScore = -compareD;
                    break;
            }
        }

        if (compareScore == 0) {
            int halfRow = (int) Math.floor(this.M / 2);

            int distanceA = Math.abs(this.move - halfRow);
            int distanceB = Math.abs(m.move - halfRow);

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

    public String toString() {
        String scoreString = s.toString();
        double rfactor = (nodes / Math.pow(M, depth)) * 100;
        return "Move col: " + move + "; player: " + player + "; score: { " + scoreString + " };\tperf: { d: " + depth
                + " n: " + nodes + " cut: " + cutoff + " rfator: " + rfactor + "% hit " + hit + " }";
    }
}
