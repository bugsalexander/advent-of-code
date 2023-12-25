/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day10;

import aoc.util.Pair;
import java.util.Arrays;

/**
 * never eat soggy waffles!
 */
public enum Direction {
    North(-1, 0),
    South(+1, 0),
    East(0, +1),
    West(0, -1);

    static {
        // post processing to set opposite
        for (Direction d : Direction.values()) {
            d.opposite = Arrays.stream(Direction.values())
                    .filter(opp -> -opp.getColDiff() == d.getColDiff() && -opp.getRowDiff() == d.getRowDiff())
                    .findFirst()
                    .orElseThrow();
        }
    }

    private final int colDiff;
    private final int rowDiff;
    private Direction opposite;

    Direction(int rowDiff, int colDiff) {
        this.rowDiff = rowDiff;
        this.colDiff = colDiff;
    }

    public int getColDiff() {
        return colDiff;
    }

    public int getRowDiff() {
        return rowDiff;
    }

    public Direction getOpposite() {
        return opposite;
    }

    public Pair<Double, Double> asVector() {
        return Pair.of((double) rowDiff, (double) colDiff);
    }
}
