package connectx.ForzaForza;

import connectx.CXPlayer;
import connectx.CXBoard;
import connectx.CXGameState;
import connectx.CXCell;
import connectx.CXCellState;
import java.util.TreeSet;
import java.util.Random;
import java.util.Arrays;

public class Evaluation {
    private CXBoard B;
    private CXCellState[][] board;
    private CXCellState playerCell;
    private CXCellState notPlayerCell;
    private int player;
    CXGameState state;

    public Evaluation(CXBoard B){
        this.B = B;
        this.board = B.getBoard();
        this.player = B.currentPlayer();
        this.state = B.gameState();
        CXCellState playerCell = player == 0 ? CXCellState.P1 : CXCellState.P2;
        CXCellState notpPlayerCell = player == 0 ? CXCellState.P2 : CXCellState.P1;
    }

    public Score evaluate() {
        int value = evaluateBoard();
        Score s = new Score(value, state);
        return s;
	}

    private int evaluateBoard() {
        int value = 0;
        
        for (int i = 0; i < B.M; i++) {
            int j = getFirstPlayerCell(i);
            if (j != -1) {
                value += possibleScore(i, j);
            }
        }
        return value;
    }

    private int possibleScore(int i, int j) {
        int score = 0;
        score += possibleScoreD(i, j, 1, 0); // destra
        score += possibleScoreD(i, j, -1, 0); // sinistra
        score += possibleScoreD(i, j, 0, 1); // alto
        score += possibleScoreD(i, j, 1, 1); // diagonale destra alto
        score += possibleScoreD(i, j, 1, -1); // diagonale destra basso
        score += possibleScoreD(i, j, -1, 1); // diagonale sinistra alto
        score += possibleScoreD(i, j, -1, -1); // diagonale sinistra basso
        return score;
    }

    private int possibleScoreD(int i, int j, int deltaI, int deltaJ) {
        int space = 0;
        int nOcc = 0;
        while (((i < B.M && i >= 0) && (j < B.N && j >= 0)) && (board[i][j] != notPlayerCell) && (space <= B.X)) {
            if (board[i][j] == playerCell) nOcc++;
            space++;
            i += deltaI;
            j += deltaJ;
        }
        if (space >= B.X) return nOcc;
        else return 0;
    }

    // return the fisrt index player cell in row, -1 otherwise
    private int getFirstPlayerCell(int colum) {
        int i = 0;

        for (i = 0; i < B.N; i++) {
            if (board[colum][i] != CXCellState.FREE) break;
        }

        if (board[colum][i] == playerCell) return i;
        else return -1;
    }
}
