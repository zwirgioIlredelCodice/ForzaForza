package connectx.ForzaForza;

import connectx.CXGameState;

public class Score implements Comparable<Score> {
    public int score;
    public CXGameState state;

    public Score(int score, CXGameState state) {
        this.score = score;
        this.state = state;
    }

    public int compareTo(Score s) {
        if (this.state == s.state) {
            switch (this.state) {
                case OPEN:
                    return compareScore(this.score, s.score);
                case DRAW:
                    return 0;
                case WINP1:
                    return 0;
                case WINP2:
                    return 0;
                default:
                    return 0;
            }
        } else {
            return compareState(this.state, s.state);
        }

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
                    default:
                        return 0;
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
                    default:
                        return 0;
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
                    default:
                        return 0;
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
                    default:
                        return 0;
                }
        }
        return 0;
    }

    private int compareScore(int s1, int s2) {
        if (s1 > s2)
            return 1;
        else if (s1 < s2)
            return -1;
        else
            return 0;
    }

    public String toString() {
        String stateString = "";
        switch (state) {
            case OPEN:
                stateString = "OPEN";
                break;
            case DRAW:
                stateString = "DRAW";
                break;
            case WINP1:
                stateString = "WINP1";
                break;
            case WINP2:
                stateString = "WINP2";
                break;
        }
        return "score: " + score + "; state: " + stateString + ";";
    }
}
