/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day10;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public enum Pipe {
    Vertical('|'),
    Horizontal('-'),
    BendNorthEast('L'),
    BendNorthWest('J'),
    BendSouthWest('7'),
    BendSouthEast('F'),
    Ground('.'),
    Start('S');

    private final char c;

    private Pipe(char c) {
        this.c = c;
    }

    public static Pipe fromChar(char c) {
        for (Pipe pipe : Pipe.values()) {
            if (pipe.c == c) {
                return pipe;
            }
        }
        throw new IllegalArgumentException("provided char does not correspond to any pipe");
    }
}
