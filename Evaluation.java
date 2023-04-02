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

    public Score evaluate(CXBoard B) {
        CXGameState state = B.gameState();
        int value = 0;
        Score s = new Score(value, state);

        return s;
	}
}
