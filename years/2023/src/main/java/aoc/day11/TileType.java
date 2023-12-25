/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day11;

public enum TileType {
    Galaxy('#'),
    EmptySpace('.');

    private char c;
    TileType(char c) {
        this.c = c;
    }

    public static TileType fromChar(int c) {
        char cc = (char) c;
        for (TileType t : TileType.values()) {
            if (t.c == cc) {
                return t;
            }
        }
        throw new IllegalStateException("unable to parse type from value");
    }
}
