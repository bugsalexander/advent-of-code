/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day03;

public class NumberIndexRange {
    private int value;
    private int startIndex;
    private int endIndex;

    public NumberIndexRange(int value, int startIndex, int endIndex) {
        this.value = value;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public NumberIndexRange setStartIndex(int startIndex) {
        this.startIndex = startIndex;
        return this;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public NumberIndexRange setEndIndex(int endIndex) {
        this.endIndex = endIndex;
        return this;
    }

    public int getValue() {
        return value;
    }

    public NumberIndexRange setValue(int value) {
        this.value = value;
        return this;
    }
}
