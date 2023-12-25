/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day10;

import java.util.Optional;

public enum TileType {
    Vertical('|', Pipe.Vertical),
    Horizontal('-', Pipe.Horizontal),
    BendNorthEast('L', Pipe.BendNorthEast),
    BendNorthWest('J', Pipe.BendNorthWest),
    BendSouthWest('7', Pipe.BendSouthWest),
    BendSouthEast('F', Pipe.BendSouthEast),
    Ground('.'),
    Start('S');

    private final char c;
    private final Pipe pipe;

    TileType(char c, Pipe p) {
        this.c = c;
        this.pipe = p;
    }

    TileType(char c) {
        this(c, null);
    }

    public static TileType fromChar(char c) {
        for (TileType tileType : TileType.values()) {
            if (tileType.c == c) {
                return tileType;
            }
        }
        throw new IllegalArgumentException("provided char does not correspond to any pipe");
    }

    public Optional<Pipe> getPipe() {
        return Optional.ofNullable(pipe);
    }

    public char getChar() {
        return c;
    }
}
