package connectx.ForzaForza;

import connectx.CXGameState;


/**
 * classe che definisce il punteggio di una certa scacchiera e che 
 * si occupa di confrontare i punteggi grazie all'implementazione della interfaccia Comparable
 * 
 * al suo interno c'è un campo intero score che associa un numero positivo se la posizione di gioco è 
 * a favore del giocatore 1 o minore di zero altrimenti e lo stato di gioco
 */
public class Score implements Comparable<Score> {
    public int score;
    public CXGameState state;

    public Score(int score, CXGameState state) {
        this.score = score;
        this.state = state;
    }

    /**
     * il metodo compareTo confronta i punteggi dal punto di vista del giocatore 1
     */
    public int compareTo(Score s) {
        if (this.state == s.state) {
            if (this.state == CXGameState.OPEN) {
                return compareScore(this.score, s.score);
            }
            else {
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
                        return 1; // il gioco aperto viene preferito alla patta
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
