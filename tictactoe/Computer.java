package tictactoe;

import tictactoe.Cell;
import tictactoe.Game;
import tictactoe.UserInterface;

import java.util.ArrayList;
import java.util.Random;


public class Computer extends Player {
    private Game game;
    private final int DIFFICULTY;

    public Computer(char symbol, int difficulty) {
        super(symbol);
        DIFFICULTY = (difficulty < 0 || difficulty > 2) ? 0 : difficulty;
    }
    

    private String getDifficulty() {
        return DIFFICULTY == 0 ? "easy" : DIFFICULTY == 1 ? "medium" : "hard";
    }

    @Override
    public void makeMove(Game game) {
        this.game = game;   // refresh game state
        UserInterface.printLine(String.format("Making move level \"%s\"", getDifficulty()));
        Cell move = getCellDependingOnDifficulty();
        game.placeSymbol(move.getX(), move.getY());
    }

    private Cell getCellDependingOnDifficulty() {
        if (DIFFICULTY == 0) return hard(); // easy()
        else if (DIFFICULTY == 1) return hard();  // medium()
        else return hard();
    }
    
    
    // easy() and medium() removed for simplicity
    
    //! Possible combinations: 255168   <-  Unable to get this number, closest was ~210k


    private Cell hard() {
        Cell[][] boardCopy = game.getBoard().clone();
        int bestScore = Integer.MIN_VALUE;
        Cell bestMove = null;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Cell currentCell = boardCopy[i][j];
                if (currentCell.isFree()) {
                    currentCell.setSymbol(getSymbol()); // place marker
                    int score = minimax(boardCopy, 0, true);
                    currentCell.setSymbol('_'); // remove marker

                    
                    if (score > bestScore) {  //! This only gets updated once... why?
                        bestScore = score;
                        bestMove = currentCell;
                    }
                }
            }
        }

        // Cell X, Y has a score of Z, and total depth of ...;
        System.out.printf("Best move: (%d, %d)\n", bestMove.getX(), bestMove.getY());
        return bestMove;
    }

    private int minimax(Cell[][] board, int depth, boolean isMaximising) {
        
        // are we at terminal state?
        char winner = game.checkWinner(board);
        
        if (winner != 'R') {    // R = still running
            if (winner == getSymbol()) {    // ai is winner
                return 10 - depth;
            }
            if (winner == getOpponent()) {  // opponent is winner
                return -10 + depth;
            }
            return 0; // draw
        }

        if (isMaximising) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    Cell currentCell = board[i][j];
                    if (currentCell.isFree()) {
                        currentCell.setSymbol(getSymbol());
                        bestScore = Math.max(bestScore, minimax(board, depth + 1, false));
                        currentCell.setSymbol('_'); // reset cell
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    Cell currentCell = board[i][j];
                    if (currentCell.isFree()) {
                        currentCell.setSymbol(getOpponent());
                        bestScore = Math.min(bestScore, minimax(board, depth + 1, true));
                        currentCell.setSymbol('_'); // reset cell
                    }
                }
            }
            return bestScore;
        }
    }
}