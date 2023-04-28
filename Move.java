package connectx.ForzaForza;

public class Move implements Comparable {
    public Score s;
    public int move;
    private int player;
    private int M; // righe massime

    public int depth;
    public int nodes;
    public int cutoff;

    public Move(int move, Score s, int player, int M,int depth, int nodes, int cutoff) {
        this.s = s;
        this.move = move;
        this.player = player;
        this.M = M;
        this.depth = depth;
        this.nodes = nodes;
        this.cutoff = cutoff;
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

    public String toString() {
        String scoreString = s.toString();
        double rfactor = (nodes / Math.pow(M, depth)) * 100;
        return "Move col: " + move + "; player: " + player + "; score: { " + scoreString + " };\tperf: { d: " + depth + " n: " + nodes + " cut: " + cutoff + " rfator: " + rfactor + "% }"; 
    }
}
