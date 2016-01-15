package com.katas.eightqueens;

import junit.framework.TestCase;

public class SolverAcceptanceTests extends TestCase {
    public void test4QueensPuzzle() throws Exception {
        assertEquals(2, Solver.Solve(4));
    }

    public void test8QueensPuzzle() throws Exception {
        assertEquals(92, Solver.Solve(8));
    }

    public void test12QueenPuzzle() throws Exception {
        assertEquals(Solver.Solve(12), 14200);
    }
}
