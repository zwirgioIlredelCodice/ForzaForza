package connectx.ForzaForza;

/**
 * BitBoard
 */
public interface BitBoard <T> {
    public void markBit(int row, int col, int player);
    public void unmarkBit(int row, int col, int player);
    public T getKey();
}