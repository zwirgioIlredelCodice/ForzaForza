package connectx.ForzaForza;

import java.util.LinkedList;

public class ScoreBoard {
    LinkedList<ScoreSet> sb[][];
    public int totalScore;

    int M,N,X;

    public int sus;

    public ScoreBoard(int M, int N, int X) {
        this.M = M;
        this.N = N;
        this.X = X;

        sus = 0;

        sb = new LinkedList[M][N];

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                sb[i][j] = new LinkedList<ScoreSet>();
            }
        }
        initBoard();
    }

    private void initBoard() {
        for (int i = 0; i < M; i++) { // row y
            for (int j = 0; j < N; j++) { // collum x
                
                if (M - i >= X) { // in giu
                    sus++;
                    ScoreSet sc = new ScoreSet();
                    for (int k = i; k < X; k++) sb[k][j].add(sc);
                }
                if (N - j >= X) { // in avanti
                    sus++;
                    ScoreSet sc = new ScoreSet();
                    for (int k = j; k < X; k++) sb[i][k].add(sc);
                }
                if ((M - i >= X) && (N - j >= X)) { // avanti giu
                    sus++;
                    ScoreSet sc = new ScoreSet();
                    int kk = j;
                    for (int k = i; k < X; k++) {
                        sb[k][kk].add(sc);
                        kk++;
                    }
                }
                if ((i + 1 >= X) && (N - j >= X)) { // avanti su
                    sus++;
                    ScoreSet sc = new ScoreSet();
                    int kk = j;
                    for (int k = i; i-k < X; k--) {
                        sb[k][kk].add(sc);
                        kk++;
                    }
                }
            }
        }
    }

    public void move(int m, int n, int player) {
        LinkedList<ScoreSet> cell = sb[m][n];

        int deltaScore = 0;
        for (ScoreSet scoreSet : cell) {
            int ps = scoreSet.score;
            scoreSet.add(player);
            deltaScore += scoreSet.score - ps;
        }
        totalScore += deltaScore;
    }

    public void unmove(int m, int n, int player) {
        LinkedList<ScoreSet> cell = sb[m][n];

        int deltaScore = 0;
        for (ScoreSet scoreSet : cell) {
            int ps = scoreSet.score;
            scoreSet.sub(player);
            deltaScore += scoreSet.score - ps;
        }
        totalScore += deltaScore;
    }

    public static void main(String[] args) {
        System.out.println("Hello World");

        ScoreBoard cacca = new ScoreBoard(3, 2, 2);
        System.out.println(cacca.sus);
        cacca.move(0, 0, 0);
        System.out.format("score:%d", cacca.totalScore);
        cacca.move(0, 1, 0);
        System.out.format("score:%d", cacca.totalScore);
    }
}

class ScoreSet {
    public int score, p1, p2;

    public ScoreSet() {
        score = 0;
        p1 = 0;
        p2 = 0;
    }

    private void fixScore() {
        if (p1 == 0 || p2 == 0) {
            if (p1 > p2) score = p1;
            else score = -p2;
        }
        else score = 0;
    }

    public void add(int player) {
        if (player == 0) p1++;
        else p2++;
        fixScore();
    }

    public void sub(int player) {
        if (player == 0) p1--;
        else p2--;
        fixScore();
    }
}
