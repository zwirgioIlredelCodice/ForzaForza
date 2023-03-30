package connectx.ForzaForza;

import connectx.CXPlayer;
import connectx.CXBoard;
import connectx.CXGameState;
import connectx.CXCell;
import java.util.TreeSet;
import java.util.Random;
import java.util.Arrays;

public class Evaluation {
    public Evaluation(){
    }

    public int evaluate(CXBoard B) {
        CXGameState state = B.gameState();
        int value = 0;

        switch (state) {
            case OPEN:
                value = 0;
                break;
            case DRAW:
                value = 0;
                break;
            case WINP1:
                value = Integer.MAX_VALUE;
                break;
            case WINP2:
                value = Integer.MIN_VALUE;
                break;
            default:
                break;
        }
        return value;
	}
}
