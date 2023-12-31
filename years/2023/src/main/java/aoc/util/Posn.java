/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.util;

import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

public class Posn {
    private final int row;
    private final int col;

    private Posn(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Posn of(int row, int col) {
        return new Posn(row, col);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Posn posn = (Posn) o;
        return getRow() == posn.getRow() && getCol() == posn.getCol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getCol());
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", row, col);
    }

    public boolean withinBounds(int rows, int cols) {
        return 0 <= row && row < rows && 0 <= col && col < cols;
    }

    public <T> T at(T[][] grid) {
        return grid[row][col];
    }

    public static Posn fromPair(Pair<Integer, Integer> integerIntegerPair) {
        return Posn.of(integerIntegerPair.getLeft(), integerIntegerPair.getRight());
    }

    public static Posn add(Posn a, Posn b) {
        return Posn.of(a.getRow() + b.getRow(), a.getCol() + b.getCol());
    }
}
