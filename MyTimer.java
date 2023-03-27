package connectx.ForzaForza;
import java.util.concurrent.TimeoutException;


public class MyTimer {
    private int  TIMEOUT;
	private long START;

    public MyTimer(int timeout) {
        this.TIMEOUT = timeout;
    }
    public void reset() {
        start();
    }
    public void start() {
        START = System.currentTimeMillis();
    }
    public void checktime() throws TimeoutException {
		if ((System.currentTimeMillis() - START) / 1000.0 >= TIMEOUT * (90.0 / 100.0))
			throw new TimeoutException();
	}
}
