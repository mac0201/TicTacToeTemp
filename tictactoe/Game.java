package tictactoe;

import tictactoe.Computer;
import tictactoe.Player;
import tictactoe.UserInterface;

import java.util.ArrayList;


public class Game {

    enum State {
        MENU,
        WIN_X,
        WIN_O,
        DRAW,
        RUNNING
    }

    private final Cell[][] BOARD;
    private final static int SIZE = 3;
    private final ArrayList<Cell> freeCells;
    private State STATE;
    int totalMoves = 0;

    private final Player[] players;
    private int currentPlayer;
    private char currentPlayerSymbol = 'X';

    public Game() {
        BOARD = new Cell[SIZE][SIZE];
        STATE = State.MENU;
        currentPlayer = 0;
        freeCells = new ArrayList<>(SIZE * SIZE);
        players = new Player[2];
        fillBoard();
    }

    public Cell[][] getBoard() {
        return BOARD;
    }

    public void play() {

        while (STATE == State.MENU) {
            String[] command = UserInterface.promptForMenuCommands();

            if (command == null) {
                UserInterface.printLine("Bad parameters!");
                continue;
            }

            if (command[0].equals("exit")) return;

            int playerCounter = 0;
            for (String parameter : command) {
                char symbol = playerCounter == 0 ? 'X' : 'O';
                switch (parameter) {
                    case "user" -> players[playerCounter] = new Player(symbol);
                    case "easy" -> players[playerCounter] = new Computer( symbol,0);
                    case "medium" -> players[playerCounter] = new Computer(symbol, 1);
                    case "hard" -> players[playerCounter] = new Computer(symbol, 2);
                    default -> throw new RuntimeException("Error creating players");
                }
                playerCounter++;
            }
            // update state to RUNNING
            STATE = State.RUNNING;
        }

        printBoard();

        while (STATE == State.RUNNING) {
            players[currentPlayer].makeMove(this);
            totalMoves++;
            printBoard();

            if (freeCells.size() <= 4) {
                char winner = checkWinner(BOARD);
                if (winner == 'D')  STATE = State.DRAW;
                else if (winner != 'R') setWin();
            }

            detectStateChange();
            switchPlayer();
        }
    }

    // Utility function for minimax
    public int countFreeCells(Cell[][] board) {
        int free = 0;
        for (Cell[] row : board) {
            for (Cell cell : row) {
                if (cell.isFree()) free++;
             }
        }
        return free;
    }


    // Fills board with empty cells (_ char)
    private void fillBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Cell cell = new Cell(row, col, '_');
                BOARD[row][col] = cell;
                freeCells.add(cell);
            }
        }
    }

    public void printBoard() {
        String border = "- ".repeat(SIZE * 2 - 1);
        UserInterface.printLine(border);
        for (Cell[] row : BOARD) {
            UserInterface.print("| ");
            for (Cell cell : row) {
                char symbol = cell.getSymbol();
                UserInterface.print((symbol == '_' ? " " : symbol) + " ");
            }
            UserInterface.print("|\n");
        }
        UserInterface.printLine(border);
    }

    public void placeSymbol(int x, int y) {
        Cell cell = getCellFromCoordinates(x, y);
        cell.setSymbol(getCurrentPlayerSymbol());
        getFreeCells().remove(cell);
//        switchPlayer();
    }

    public boolean validCoordinates(Integer[] coordinates) {
        int x = coordinates[0];
        int y = coordinates[1];
        if (!(x >= 0 && x < SIZE && y >= 0 && y < SIZE)) {
            UserInterface.printLine("Coordinates should be from 1 to 3!");
            return false;
        }
        if (!BOARD[x][y].isFree()) {
            UserInterface.printLine("This cell is occupied! Choose another one!");
            return false;
        }
        return true;
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer == 0 ? 1 : 0;
        currentPlayerSymbol = currentPlayer == 0 ? 'X' : 'O';
    }

    private void setWin() {
        System.out.println("WINNER!");
        if (currentPlayer == 0) {
            STATE = State.WIN_X;
        } else {
            STATE = State.WIN_O;
        }
    }

    private void detectStateChange() {
        UserInterface.print(
            switch (STATE) {
                case WIN_X -> "X wins\n";
                case WIN_O -> "O wins\n";
                case DRAW -> "Draw\n";
                case RUNNING, MENU -> "";
            }
        );
    }


    private boolean equalsThree(char a, char b, char c) {
        return a == b && b == c & a != '_';
    }

    /*
    * Checks for winner in current board state
    * Returns char
    *       R - game still in progress
    *       D - draw
    *       X - X wins
    *       O - O wins
    * */
    public char checkWinner(Cell[][] board) {
        char winner = 'R'; // running
        // horizontal
        for (int row = 0; row < 3; row++) {
            if (equalsThree(board[row][0].getSymbol(), board[row][1].getSymbol(), board[row][2].getSymbol())) {
                winner = board[row][0].getSymbol();
            }
        }

        // vertical
        for (int col = 0; col < 3; col++) {
            if (equalsThree(board[0][col].getSymbol(), board[1][col].getSymbol(), board[2][col].getSymbol())) {
                winner = board[0][col].getSymbol();
            }
        }

        // diagonal
        if (equalsThree(board[0][0].getSymbol(), board[1][1].getSymbol(), board[2][2].getSymbol())) {
            winner = board[0][0].getSymbol();
        }
        if (equalsThree(board[2][0].getSymbol(), board[1][1].getSymbol(), board[0][2].getSymbol())) {
            winner = board[2][0].getSymbol();
        }

        int openSpots = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col].isFree()) openSpots++;
            }
        }

        if (winner == 'R' && openSpots == 0) {
            return 'D';
        }

        return winner;
    }


    private Cell getCellFromCoordinates(int x, int y) {
        return BOARD[x][y];
    }

    public char getCurrentPlayerSymbol() {
        return currentPlayerSymbol;
    }

    public ArrayList<Cell> getFreeCells() {
        return freeCells;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
