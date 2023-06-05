package connectx.ForzaForza;

//import java.util.Random;
import java.security.SecureRandom;

public class ZobristHash {

    private int M,N;

    private long randomnums[][][];
    private SecureRandom random;

    private long hashKey;
    
    public ZobristHash(int M, int N) {
        this.M = M;
        this.N = N;
        hashKey = 0;

        randomnums = new long[M][N][2];
        random = new SecureRandom();
        initRandom();
        random.setSeed(104707); // per essere riproducibile
    }

    private void initRandom() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                randomnums[i][j][0] = random.nextLong();
                randomnums[i][j][1] = random.nextLong();
            }
        }
    }

    public void updateHash(int row, int col, int player) {
        hashKey ^= randomnums[row][col][player];
    }

    public long getHash() {
        return hashKey;
    }
}
