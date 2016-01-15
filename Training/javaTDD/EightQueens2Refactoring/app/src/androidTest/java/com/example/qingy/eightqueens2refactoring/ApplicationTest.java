package com.example.qingy.eightqueens2refactoring;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testQueenPuzzle_4() {
        assertEquals(QueenSolver.SolveCount(4), 2);
    }

    public void testQueenPuzzle_8() {
        assertEquals(QueenSolver.SolveCount(8), 92);
    }

    public void testQueenPuzzle_10() {
        assertEquals(QueenSolver.SolveCount(10), 724);
    }
    public void testQueenPuzzle_12() {
        assertEquals(QueenSolver.SolveCount(12), 14200);
    }
//    public void testQueenPuzzle_13() {
//        assertEquals(QueenSolver.SolveCount(13), 73712L);
//    }

}