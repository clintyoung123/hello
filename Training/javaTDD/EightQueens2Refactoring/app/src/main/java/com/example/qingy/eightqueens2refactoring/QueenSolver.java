package com.example.qingy.eightqueens2refactoring;

/**
 * Created by qingy on 1/14/16.
 * https://en.wikipedia.org/wiki/Eight_queens_puzzle
 */
public class QueenSolver {
    private static int numSolns;

    private static boolean[][] board = null;
    private static boolean[] colOccupied = null;
    private static boolean[] diag1Occupied = null;
    private static boolean[] diag2Occupied = null;

    public static int SolveCount(int n) {
        int SIZE = n;
        board = new boolean[SIZE][SIZE];
        colOccupied = new boolean[SIZE];
        diag1Occupied = new boolean[SIZE * 2 - 1];
        diag2Occupied = new boolean[SIZE * 2 - 1];
        numSolns = 0;

        Solve(0, SIZE);
        return numSolns;
    }

    static void Solve(int row, int SIZE) // assumes all rows before "row" have correctly-placed queens so far
    {
        if (row < SIZE)
        {
            for (int col = 0; col < SIZE; col++)
            {
                if (!colOccupied[col])
                {
                    int diag1 = row + col;
                    if (!diag1Occupied[diag1])
                    {
                        int diag2 = SIZE - row + col - 1;
                        if (!diag2Occupied[diag2])
                        {
                            board[row][col] = colOccupied[col] = diag1Occupied[diag1] = diag2Occupied[diag2] = true;
                            Solve(row + 1, SIZE);
                            board[row][col] = colOccupied[col] = diag1Occupied[diag1] = diag2Occupied[diag2] = false;
                        }
                    }
                }
            }
        }
        else
        {
            numSolns++;
        }
    }
}
