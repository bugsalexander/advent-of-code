/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day08;

public enum Direction {
    Left('L'),
    Right('R');

    private final char character;
    private Direction(char character) {
        this.character = character;
    }

    public static Direction fromChar(char character) {
        for (Direction d : Direction.values()) {
            if (d.character == character) {
                return d;
            }
        }
        throw new IllegalArgumentException("no direction exists for character " + character);
    }
}
