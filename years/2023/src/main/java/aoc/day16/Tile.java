/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day16;

import aoc.util.GenericTile;

public enum Tile implements GenericTile {
    Empty('.'),
    Mirror1('/'),
    Mirror2('\\'),
    Splitter1('-'),
    Splitter2('|'),
    ;

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
