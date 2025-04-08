package ui;

import chess.ChessPosition;

/**
 * The `MoveInputParser` class provides a method to parse a chess move input string
 * and convert it into a `ChessPosition` object.
 */
public class MoveInputParser {
    public static ChessPosition parsePosition(String input) throws IllegalArgumentException {
        if (input == null || input.length() != 2) {
            throw new IllegalArgumentException("Invalid coordinate: " + input);
        }

        char colChar = input.charAt(0);
        char rowChar = input.charAt(1);
        int col = colChar - 'a' + 1;

        if (colChar < 'a' || colChar > 'h' || rowChar < '1' || rowChar > '8') {
            throw new IllegalArgumentException("Invalid coordinate: " + input);
        }

        return new ChessPosition(Character.getNumericValue(rowChar), col);
    }
}
