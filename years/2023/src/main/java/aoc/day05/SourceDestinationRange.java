/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day05;

public class SourceDestinationRange {
    private int destinationStart;
    private int sourceStart;
    private int range;

    public SourceDestinationRange(int destinationStart, int sourceStart, int range) {
        this.destinationStart = destinationStart;
        this.sourceStart = sourceStart;
        this.range = range;
    }

    public int getDestinationStart() {
        return destinationStart;
    }

    public SourceDestinationRange setDestinationStart(int destinationStart) {
        this.destinationStart = destinationStart;
        return this;
    }

    public int getSourceStart() {
        return sourceStart;
    }

    public SourceDestinationRange setSourceStart(int sourceStart) {
        this.sourceStart = sourceStart;
        return this;
    }

    public int getRange() {
        return range;
    }

    public SourceDestinationRange setRange(int range) {
        this.range = range;
        return this;
    }
}
