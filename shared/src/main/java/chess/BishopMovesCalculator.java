package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class BishopMovesCalculator {

    public static Collection<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition position){
        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();
        //traverse the board and find all possible end positions (not accounting for pieces in the way)
        for(int y = 1; y <= 8; y++){
            for(int x = 1; x <= 8; x++){
                int offset = Math.abs(x - position.getColumn());
                if (offset == Math.abs(y - position.getRow())){
                    ChessPosition indexPosition = new ChessPosition(x,y);
                    possiblePositions.add(indexPosition);
                }
            }
        }

        //TODO: how to check for pieces in the way and eliminate end positions beyond those pieces?
        return null;
    }

}
