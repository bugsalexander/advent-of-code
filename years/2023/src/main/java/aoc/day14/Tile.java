/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day14;

import aoc.util.GenericTile;

public enum Tile implements GenericTile {
    RoundRock('O'),
    CubeRock('#'),
    Empty('.');

    private final char c;
    Tile(char c) {
        this.c = c;
    }

    @Override
    public char getChar() {
        return c;
    }

    @Override
    public String toString() {
        return String.valueOf(c);
    }
}
