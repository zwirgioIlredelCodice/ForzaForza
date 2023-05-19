package connectx.ForzaForza;

public class Move implements Comparable<Move> {
    public Score s;
    public int move;
    private int player;
    private int N; // righe massime

    public int depth;
    public int nodes;
    public int cutoff;
    public int hit;

    public Move(int move, Score s, int player, int N, int depth, int nodes, int cutoff, int hit) {
        this.s = s;
        this.move = move;
        this.player = player;
        this.N = N;
        this.depth = depth;
        this.nodes = nodes;
        this.cutoff = cutoff;
        this.hit = hit;
    }

    public Move(int move, Score s, int player, int N) {
        this.s = s;
        this.move = move;
        this.player = player;
        this.N = N;
        this.depth = 0;
        this.nodes = 0;
        this.cutoff = 0;
        this.hit = 0;
    }

    /*
     * mette a confronto 2 mosse dal punto di vista del giocatore che deve
     * massimizzare
     */
    public int compareTo(Move m) {

        int compareScore = this.s.compareTo(m.s);

        if (compareScore == 0) {
            int halfRow = (int) Math.floor(this.N / 2);

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
        double rfactor = (nodes / Math.pow(N, depth)) * 100;
        return "Move col: " + move + "; player: " + player + "; score: { " + scoreString + " };\tperf: { d: " + depth
                + " n: " + nodes + " cut: " + cutoff + " rfator: " + rfactor + "% hit " + hit + " }";
    }
}
