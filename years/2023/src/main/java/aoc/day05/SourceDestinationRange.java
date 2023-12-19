/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day05;

import java.math.BigInteger;

public class SourceDestinationRange {
    private BigInteger destinationStart;
    private BigInteger sourceStart;
    private BigInteger range;

    public SourceDestinationRange(BigInteger destinationStart, BigInteger sourceStart, BigInteger range) {
        this.destinationStart = destinationStart;
        this.sourceStart = sourceStart;
        this.range = range;
    }

    public BigInteger getDestinationStart() {
        return destinationStart;
    }

    public SourceDestinationRange setDestinationStart(BigInteger destinationStart) {
        this.destinationStart = destinationStart;
        return this;
    }

    public BigInteger getSourceStart() {
        return sourceStart;
    }

    public SourceDestinationRange setSourceStart(BigInteger sourceStart) {
        this.sourceStart = sourceStart;
        return this;
    }

    public BigInteger getRange() {
        return range;
    }

    public SourceDestinationRange setRange(BigInteger range) {
        this.range = range;
        return this;
    }
}
