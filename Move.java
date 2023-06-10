package connectx.ForzaForza;

import connectx.CXGameState;

/**
 * classe che definisce una mossa giocabile e che si occupa 
 * di confrontare le mosse grazie all'implementazione della 
 * interfaccia Comparable
 * 
 * una mossa viene valutata per: il punteggio che può portare al giocatore se viene fatta, 
 * la colonna in cui viene fatta per la profondità della valutazione.
 * 
 * tutti i metodi di questa classe hanno un costo costante O(1)
 */
public class Move implements Comparable<Move> {
    public Score s; // la valutazione della mossa
    public int move; // colonna in cui si mette la pedina
    private int player; // il giocatore che la esegue
    private int N; // colonne massime
    public int depth; // profondità della valutazione

    // variabili per il debug
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

    private int compareDepth(int d1, int d2) {
        if (d1 == d2)
            return 0;
        else if (d1 < d2)
            return -1;
        else
            return 1;
    }

    /*
     * mette a confronto le mosse dal punto di vista del giocatore 1
     */
    public int compareTo(Move m) {

        int compareScore = this.s.compareTo(m.s);

        if (compareScore == 0 && this.s.state == CXGameState.OPEN) {
            int halfRow = (int) Math.floor(this.N / 2);

            int distanceA = Math.abs(this.move - halfRow);
            int distanceB = Math.abs(m.move - halfRow);

            // preferisce le mosse vicine al centro della scacchiera
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
            if (compareScore != 0) {
                return compareScore;
            } else {
                int compareD = compareDepth(this.depth, m.depth);
                switch (this.s.state) {
                    case OPEN:
                        return 0;
                    case DRAW:
                        return compareD; // ritarda la resa il pù possibile
                    case WINP1:
                        return -compareD; // anticipa la vittoria il più possibile
                    case WINP2:
                        return compareD; // ritarda la sconfitta il più possibile
                    default:
                        return 0;
                }
            }
        }
    }

    public String toString() {
        String scoreString = s.toString();
        double rfactor = (nodes / Math.pow(N, depth)) * 100;
        return "Move col: " + move + "; player: " + player + "; score: { " + scoreString + " };\tperf: { d: " + depth
                + " n: " + nodes + " cut: " + cutoff + " rfator: " + rfactor + "% hit " + hit + " }";
    }
}
