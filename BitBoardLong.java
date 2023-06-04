package connectx.ForzaForza;

public class BitBoardLong implements BitBoard<Long> {
    
    long p1;
    long all;

    int M;
    int N;

    public BitBoardLong(int M, int N) {
        p1 = 0; // 1 se c'è la pedina di p1 0 altrimenti
        all = 0; // p1 and p2

        this.M = M;
        this.N = N;
    }

    public void markBit(int row, int col, int player) {
        int n = row * M + col;
        if (player == 0) {
            p1 = flipBit(p1, n);
        }
        all = flipBit(p1, n);
    }

    public void unmarkBit(int row, int col, int player) {
        markBit(row, col, player);
    }

    private long flipBit(long num, int index) {
        return num ^ (1<<index);
    }

    public Long getKey() {
        Long key = all*all + p1;
        return key;
    }
}
