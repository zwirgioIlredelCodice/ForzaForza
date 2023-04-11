package connectx.ForzaForza;

import connectx.CXPlayer;
import connectx.CXBoard;
import connectx.CXGameState;
import connectx.CXCell;
import connectx.CXCellState;
import java.util.TreeSet;
import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class Evaluation {
    private CXBoard B;
    private CXCellState[][] board;
    private CXCellState playerCell;
    private CXCellState notPlayerCell;
    private int player;
    CXGameState state;

    MyTimer timer;

    public Evaluation(CXBoard B, MyTimer timer) throws TimeoutException {
        this.B = B;
        this.board = B.getBoard();
        timer.checktime();

        this.player = B.currentPlayer();
        this.state = B.gameState();
        CXCellState playerCell = player == 0 ? CXCellState.P1 : CXCellState.P2;
        CXCellState notPlayerCell = player == 0 ? CXCellState.P2 : CXCellState.P1;
        this.timer = timer;
    }

    public Score evaluate() throws TimeoutException {
        int value = evaluateBoard();
        Score s = new Score(value, state);
        return s;
    }

    private int evaluateBoard() throws TimeoutException {
        int value = 0;

        for (int i = 0; i < B.M; i++) {
            timer.checktime();
            int j = getFirstPlayerCell(i);
            if (j != -1) {
                value += possibleScore(i, j);
            }
        }
        return value;
    }

    private int getFirstPlayerCell(int colum) throws TimeoutException {
        int i = 0;
        for (i = 0; i < B.N; i++) {
            timer.checktime();
            if (board[colum][i] != CXCellState.FREE)
                return i;
        }
        return -1;
    }

    private int possibleScore(int i, int j) throws TimeoutException {
        CXCellState s = board[i][j];
        int n;
        int alreadyFilled;

        int score = 0;

        int M = B.M;
        int N = B.N;
        int X = B.X;

        // Useless pedantic check
        if (s == CXCellState.FREE)
            return 0;

        // Horizontal check
        n = 1;
        alreadyFilled = 1;
        for (int k = 1; j - k >= 0 && board[i][j - k] != notPlayerCell; k++) {
            timer.checktime();
            if (board[i][j - k] == s)
                alreadyFilled++;
            n++;
        } // boardackward check
        for (int k = 1; j + k < N && board[i][j + k] != notPlayerCell; k++) {
            timer.checktime();
            if (board[i][j + k] == s)
                alreadyFilled++;
            n++;
        } // forward check
        if (n >= X)
            score += alreadyFilled;

        // Vertical check
        n = 1;
        alreadyFilled = 1;
        for (int k = 1; i + k < M && board[i + k][j] != notPlayerCell; k++) {
            timer.checktime();
            if (board[i + k][j] == s)
                alreadyFilled++;
            n++;
        }
        if (n >= X)
            score += alreadyFilled;

        // Diagonal check
        n = 1;
        alreadyFilled = 1;
        for (int k = 1; i - k >= 0 && j - k >= 0 && board[i - k][j - k] != notPlayerCell; k++) {
            timer.checktime();
            if (board[i - k][j - k] == s)
                alreadyFilled++;
            n++;
        } // boardackward check
        for (int k = 1; i + k < M && j + k < N && board[i + k][j + k] != notPlayerCell; k++) {
            timer.checktime();
            if (board[i + k][j + k] == s)
                alreadyFilled++;
            n++;
        } // forward check
        if (n >= X)
            score += alreadyFilled;

        // Anti-diagonal check
        n = 1;
        alreadyFilled = 1;
        for (int k = 1; i - k >= 0 && j + k < N && board[i - k][j + k] != notPlayerCell; k++) {
            timer.checktime();
            if (board[i - k][j + k] == s)
                alreadyFilled++;
            n++;
        } // boardackward check
        for (int k = 1; i + k < M && j - k >= 0 && board[i + k][j - k] != notPlayerCell; k++) {
            timer.checktime();
            if (board[i + k][j - k] == s)
                alreadyFilled++;
            n++;
        } // forward check
        if (n >= X)
            score += alreadyFilled;

        if (s == CXCellState.P2)
            score = -score;
        return score;
    }
}
