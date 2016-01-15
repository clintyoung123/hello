package com.katas.eightqueens;

import java.util.ArrayList;

public class Board {

    public class Location
    {
        public int col;
        public int row;

        public Location(int c, int r)
        {
            col = c;
            row = r;
        }
    }

    ArrayList<Location> queenLocations = new ArrayList<Location>();

    public boolean isSpotUnderAttack(int col, int row) {
        for (Location l:queenLocations){
            if ((l.col == col) || (l.row == row)) {
                return true;
            }

            if (Math.abs(l.col - col) == Math.abs(l.row - row)){
                return true;
            }
        }
        return false;
    }

    public void placeQueen(int col, int row) {
        queenLocations.add(new Location(col, row));
    }

    public void removeQueen(int col, int row) {
        for (Location l:queenLocations){
            if ((l.col == col) || (l.row == row)) {
                queenLocations.remove(l);
            }
        }
    }
}
