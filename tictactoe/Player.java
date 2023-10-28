package tictactoe;

import tictactoe.Game;
import tictactoe.UserInterface;

public class Player {

    private final char symbol;

    public Player(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public char getOpponent() {
        return symbol == 'X' ? 'O' : 'X';
    }

    public void makeMove(Game game) {
        Integer[] coordinates;
        while (true) {
            coordinates = UserInterface.promptForCoordinates();
            if (coordinates == null || !game.validCoordinates(coordinates)) {
                continue;
            }
            break;
        }
        game.placeSymbol(coordinates[0], coordinates[1]);
    }
}