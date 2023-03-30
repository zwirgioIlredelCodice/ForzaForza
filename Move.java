package connectx.ForzaForza;

import connectx.CXGameState;

public class Move implements Comparable {
    private int score;
    private CXGameState state;

    public Move(int score, CXGameState state) {
        this.score = score;
        this.state = state;
    }

    private int compareScore(int s1, int s2) {
        if (s1 > s2)
            return 1;
        else if (s1 < s2)
            return -1;
        else
            return 0;
    }

    private int compareState(CXGameState s1, CXGameState s2) {
        switch (s1) {
            case OPEN:
                switch (s2) {
                    case OPEN:
                        return 0;
                    case DRAW:
                        return 1;
                    case WINP1:
                        return -1;
                    case WINP2:
                        return 1;
                }
            case DRAW:
                switch (s2) {
                    case OPEN:
                        return -1;
                    case DRAW:
                        return 0;
                    case WINP1:
                        return -1;
                    case WINP2:
                        return 1;
                }
            case WINP1:
                switch (s2) {
                    case OPEN:
                        return 1;
                    case DRAW:
                        return 1;
                    case WINP1:
                        return 0;
                    case WINP2:
                        return 1;
                }
            case WINP2:
                switch (s2) {
                    case OPEN:
                        return -1;
                    case DRAW:
                        return -1;
                    case WINP1:
                        return -1;
                    case WINP2:
                        return 0;
                }
            }
        return 0;
    }

    /*
    mette a confronto 2 mosse dal punto di vista del giocatore che deve massimizzare
    */
    public int compareTo(Object o) {
        if ((o != null) && (o instanceof Move)) {
            Move nm = (Move) o;

            if (this.state == nm.state) {
                switch (this.state) {
                    case OPEN:
                        return compareScore(this.score, nm.score);
                    case DRAW:
                        return 0;
                    case WINP1:
                        return -compareScore(this.score, nm.score); //prima la vittoria piu vicina
                    case WINP2:
                        return compareScore(this.score, nm.score); //prima la scofitta piu lontana
                }
            }
            else {
                return compareState(this.state, nm.state);
            }
        }
        return -1;
    }
}
