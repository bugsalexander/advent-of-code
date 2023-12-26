/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day12;

public enum SpringCondition {
    Operational('.'),
    Damaged('#'),
    Unknown('?');

    private final char c;
    SpringCondition(char c) {
        this.c = c;
    }

    public static SpringCondition fromChar(int c) {
        char cc = (char) c;
        for (SpringCondition t : SpringCondition.values()) {
            if (t.c == cc) {
                return t;
            }
        }
        throw new IllegalStateException("unable to parse type from value");
    }

    @Override
    public String toString() {
        return String.valueOf(c);
    }
}
