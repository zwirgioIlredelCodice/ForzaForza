package connectx.ForzaForza;

import java.util.concurrent.TimeoutException;

/**
 * questa classe si occupa di tenere il tempo trascorso da quando viene data
 * l'eseguzione al giocatore in modo da non utilizzare più tempo di quello permesso
 * 
 * tutti i metodi di questa classe hanno un costo costante O(1)
 */

public class MyTimer {
    private int TIMEOUT;
    private long START;

    /**
     * inizializza la classe MyTimer
     * @param timeout i secondi massimi a disposizione
     */
    public MyTimer(int timeout) {
        this.TIMEOUT = timeout;
    }

    public void start() {
        START = System.currentTimeMillis();
    }

    /**
     * metodo utilizzato per controllare che si ha a disposizione ancora tempo.
     * per lasciare del margine si utilizza il 90% del tempo a disposizione
     * @throws TimeoutException se è finito il tempo a disposizione
     */
    public void checktime() throws TimeoutException {
        if ((System.currentTimeMillis() - START) / 1000.0 >= TIMEOUT * (90.0 / 100.0))
            throw new TimeoutException();
    }

    public long getTimeElapsed() {
        return (System.currentTimeMillis() - START);
    }
}
