package tictactoe;

public class Cell {

    private final int x;
    private final int y;
    private char symbol;

    public Cell(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "" + symbol;
    }

    public boolean isFree() {
        return symbol == '_';
    }

    public void setSymbol(char newSymbol) {
        symbol = newSymbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
