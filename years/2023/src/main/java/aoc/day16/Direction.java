/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day16;

import org.apache.commons.lang3.tuple.Pair;

public enum Direction {
    Left(Pair.of(0, -1)),
    Right(Pair.of(0, +1)),
    Up(Pair.of(-1, 0)),
    Down(Pair.of(+1, 0)),
    ;

    // row, col
    private final Pair<Integer, Integer> vector;

    Direction(Pair<Integer, Integer> vector) {
        this.vector = vector;
    }

    public Pair<Integer, Integer> getVector() {
        return vector;
    }
}
