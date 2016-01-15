package com.katas.eightqueens;


/**
 * https://en.wikipedia.org/wiki/Eight_queens_puzzle
 */
public class Solver {
    public static int Solve(int n) throws Exception {
        return Solve(new Board(), 0, n);
    }

    private static int Solve(Board board, int row, int queens) {
        if (row >= queens)
            return 1;

        int count = 0;
        for(int col=0; col<queens; ++col)
        {
            if (board.isSpotUnderAttack(col, row) == false)
            {
                board.placeQueen(col, row);
                count += Solve(board, row+1, queens);
                board.removeQueen(col, row);
            }
        }
        return count;
    }
}
