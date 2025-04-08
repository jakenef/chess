package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        if (row <= 8 && col <= 8 && row >= 1 && col >= 1) {
            this.row = row;
            this.col = col;
        }else{
            throw new RuntimeException("ChessPosition was passed row="+row+" col="+col);
        }
    }

    @Override
    public String toString() {
        char file = (char) ('a' + col - 1);
        return file + String.valueOf(row);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    public static boolean isInBounds(int row, int col){
        return 0 < col && col < 9 && 0 < row && row < 9;
    }
}