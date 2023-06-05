package connectx.ForzaForza;

import connectx.CXBoard;
import connectx.CXGameState;
import connectx.CXCell;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

/**
 * questa è la classe che si occupa di tutti i calcoli per 
 * determinare la mossa mogiore.
 */

public class MoveEngine extends CXBoard {

    private MyTimer timer;

    private BitBoard bitBoard;
    private CacheTable table;

    private ScoreBoard scoreBoard;

    private Score MAX_SCORE;
    private Score MIN_SCORE;

    final boolean debug = true;

    private int perft;
    private int cutoff;
    private int hit;

    /**
     * Costo: dominato da ScoreBoard O(MNX)
     */
    public MoveEngine(int M, int N, int X, int timeout_in_secs) {
        super(M, N, X);

        this.timer = new MyTimer(timeout_in_secs);
        bitBoard = new BitBoard(M, N);
        table = new CacheTable();
        scoreBoard = new ScoreBoard(M, N, X);

        MAX_SCORE = new Score(Integer.MAX_VALUE, CXGameState.WINP1);
        MIN_SCORE = new Score(Integer.MIN_VALUE, CXGameState.WINP2);

        perft = 0;
        cutoff = 0;
        hit = 0;
    }

    /**
     * Costo: dominato da bitBoard.markBit O(MN)
     */
    @Override
    public CXGameState markColumn(int col) throws IndexOutOfBoundsException, IllegalStateException {
        int pl = currentPlayer;
        CXGameState ret = super.markColumn(col);

        CXCell lastmove = getLastMove();
        bitBoard.markBit(lastmove.i, lastmove.j, pl);
        scoreBoard.move(lastmove.i, lastmove.j, pl);

        return ret;
    }

    /**
     * Costo: dominato da bitBoard.markBit O(MN)
     */
    @Override
    public void unmarkColumn() throws IllegalStateException {
        
        CXCell lastmove = getLastMove();
        super.unmarkColumn();
        bitBoard.markBit(lastmove.i, lastmove.j, currentPlayer);
        scoreBoard.unmove(lastmove.i, lastmove.j, currentPlayer);
    }

    /*
     * questo metodo resistuisce la mossa migiore avendo a 
     * disposizione un tempo fissato.
     * 
     * il suo funzionamento consiste nel valutare l'albero di gioco ad
     * una profondità sempre crescente finchè ha tempo, le mosse dopo 
     * ogni visita vengono ordinate dalla più promettente alla meno 
     * così che nelle visite sucessive si valutino le mosse più 
     * promettenti prima 
     * 
     * Costo: O(M^N*(MN)^2)
     */
    public int IterativeDepening() {
        timer.start();

        // salva lo stato della scacchiera in modo da riportarla al valore generale in caso di una TimeoutException
        int sizeMC = MC.size();

        Integer[] L = getAvailableColumns();
        Move[] ml = new Move[L.length];
        int move = 0;

        int max_depth = numOfFreeCells();

        for (int i = 0; i < L.length; i++) { // inizzializza l'array delle mosse
            ml[i] = new Move(L[i], new Score(0, CXGameState.OPEN), currentPlayer, N, 0, 0, 0, 0);
        }

        int d = 1;
        for (d = 1; d <= max_depth; d++) {
            try {
                table.reset();

                ml = movelist(ml, d);

                // ordina le mosse
                if (currentPlayer == 0) {
                    Arrays.sort(ml, Collections.reverseOrder());
                } else {
                    Arrays.sort(ml);
                }

                move = ml[0].move;

                System.err.format("depth: %d, time: %d\n", d, timer.getTimeElapsed());
            } catch (TimeoutException e) {
                System.err.format("time finished\n");

                // annulla le mosse fatte durante una ricera interrotta per mancaza di tempo
                while(MC.size() > sizeMC) {
                    unmarkColumn();
                }
                
                if (debug) {
                    for (Move m : ml) {
                        System.err.println(m);
                    }
                }

                return move;
            }
        }

        if (debug) {
            for (Move m : ml) {
                System.err.println(m);
            }
        }

        return move;
    }

    /**
     * ritorna tutte le mosse che il giocatore può fare valutate 
     * ad una profondità depth
     * 
     * funzionamento:
     * ogni mossa viene valutata eseguendo l'algoritmo minimax
     * 
     * @param prevMl    l'array di mosse da valutare
     * @param depth     la profodità della valutazione
     * @return          l'array di mosse valutate alla profondità depth
     * 
     * Costo: O(M^depth*(MN)^2)
     */
    private Move[] movelist(Move[] prevMl, int depth) throws TimeoutException {
        Score eval;

        Score alpha = MIN_SCORE;
        Score beta = MAX_SCORE;

        if (depth <= 0) {
            return prevMl;
        }

        else if (currentPlayer == 0) {
            eval = MIN_SCORE;
            for (int i = 0; i < prevMl.length; i++) {
                timer.checktime();

                if (prevMl[i].s.state == CXGameState.OPEN) {

                    perft = 0;
                    cutoff = 0;
                    hit = 0;

                    markColumn(prevMl[i].move);

                    eval = max(eval, AlphaBeta(alpha, beta, depth - 1));
                    alpha = max(eval, alpha);

                    prevMl[i].s = eval; // update score value
                    prevMl[i].depth = depth;
                    prevMl[i].nodes = perft;
                    prevMl[i].cutoff = cutoff;
                    prevMl[i].hit = hit;

                    unmarkColumn();
                }
            }

        } else {
            eval = MAX_SCORE;
            for (int i = 0; i < prevMl.length; i++) {
                timer.checktime();

                if (prevMl[i].s.state == CXGameState.OPEN) {

                    perft = 0;
                    cutoff = 0;
                    hit = 0;

                    markColumn(prevMl[i].move);

                    eval = min(eval, AlphaBeta(alpha, beta, depth - 1));
                    beta = min(eval, beta);

                    prevMl[i].s = eval; // update score value
                    prevMl[i].depth = depth;
                    prevMl[i].nodes = perft;
                    prevMl[i].cutoff = cutoff;
                    prevMl[i].hit = hit;

                    unmarkColumn();
                }
            }
        }
        return prevMl;
    }

    /**
     * questo metodo si occupa di ordinare le mosse in modo approssimativo
     * ma molto velocemente in modo da aiutare l'algoritmo minimax con ottimizazione
     * alpa-beta pruning.
     * 
     * @param L l'array di colonne libere
     * @return  l'array di colonne libere ordinato dal più promettente al meno in modo approssimativo
     * 
     * Costo: O(nlog(n)) con n = L.lenght
     */
    private Integer[] stepSort(Integer[] L) {
        Integer[] out = new Integer[L.length];
        Move[] ma = new Move[L.length];

        if (gameState != CXGameState.OPEN) {
            return L;
        }
        for (int index = 0; index < L.length; index++) {
            this.markColumn(L[index]);
            Score s = new Score(scoreBoard.totalScore, gameState);
            ma[index] = new Move(L[index], s, currentPlayer, N);
            this.unmarkColumn();
        }

        if (currentPlayer == 0) {
            Arrays.sort(ma, Collections.reverseOrder());
        } else {
            Arrays.sort(ma);
        }

        for (int index = 0; index < L.length; index++) {
            out[index] = ma[index].move;
        }
        return out;
    }

    /**
     * ritorna il punteggio migiore per il giocatore riferito a una scacchera di gioco.
     * implementa l'algoritmo minimax con queste ottimizzazioni:
     * 
     * 1. alpha-beta pruning
     * 2. parziale riordinamento delle mosse
     * 3. salvataggio dei punteggi calcolati in una struttura simile alla cache del computer
     * 
     * @param alpha il punteggio peggiore che può avere Max
     * @param beta  il punteggio migiore che può avere Min
     * @param depth la profondità di ricerca
     * @return      il punteggio migiore per il giocatore su una scacchiera di gioco
     * @throws TimeoutException
     * 
     * Costo: O(M^depth*(MN)^2)
     */
    private Score AlphaBeta(Score alpha, Score beta, int depth) throws TimeoutException {
        
        Score eval;
        
        if (depth >= 2) {
            eval = table.get(bitBoard.getKey());
            if (eval != null) { // se la pisizione è già stata valutata in precendeza e si ha il valor salvato termina
                hit++;
                return eval;
            }
        }

        Integer[] L = getAvailableColumns();

        // riordina le mosse in modo da controllare prima le mosse più promettenti
        if (depth >= 1) {
            L = stepSort(L);
        }
        

        if (depth <= 0 || gameState != CXGameState.OPEN) {
            eval = evaluate();
            perft++;
        }

        else if (currentPlayer == 0) {
            eval = MIN_SCORE;
            for (int i : L) {
                timer.checktime();

                markColumn(i);

                eval = max(eval, AlphaBeta(alpha, beta, depth - 1));
                alpha = max(eval, alpha);

                unmarkColumn();

                if (beta.compareTo(alpha) <= 0) {
                    cutoff++;
                    break;
                }
            }

        } else {
            eval = MAX_SCORE;
            for (int i : L) {
                timer.checktime();

                markColumn(i);

                eval = min(eval, AlphaBeta(alpha, beta, depth - 1));
                beta = min(eval, beta);

                unmarkColumn();

                if (beta.compareTo(alpha) <= 0) {
                    cutoff++;
                    break;
                }
            }
        }
        
        if (depth >= 2) table.put(bitBoard.getKey(), eval);

        return eval;
    }

    private Score max(Score a, Score b) {
        if (a.compareTo(b) >= 0)
            return a;
        else
            return b;
    }

    private Score min(Score a, Score b) {
        if (a.compareTo(b) <= 0)
            return a;
        else
            return b;
    }

    /**
     * ritorna un punteggio calcolato euristicamente per la situazione di gioco corrente
     * @return un punteggio calcolato euristicamente per la situazione di gioco corrente
     */
    public Score evaluate() {
        int value = scoreBoard.totalScore;
        Score s = new Score(value, gameState);
        return s;
    }
}
