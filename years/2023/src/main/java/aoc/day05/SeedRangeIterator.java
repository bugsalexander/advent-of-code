/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day05;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SeedRangeIterator implements Iterator<BigInteger> {

    private int seedPairIndex = 0;
    private final List<BigInteger> seedPairs;
    private BigInteger end;
    private BigInteger currentValue;
    public SeedRangeIterator(List<BigInteger> seedRanges) {
        this.seedPairs = seedRanges;

        this.currentValue = seedRanges.get(seedPairIndex);
        this.end = currentValue.add(seedRanges.get(seedPairIndex + 1));
    }

    @Override
    public boolean hasNext() {
        return this.currentValue.compareTo(end) < 0;
    }

    @Override
    public BigInteger next() {
        if (!hasNext()) {
            throw new NoSuchElementException("no more elements remain");
        }

        BigInteger result = this.currentValue;
        this.currentValue = this.currentValue.add(BigInteger.ONE);
        if (this.currentValue.compareTo(end) == 0) {
            // go to the next pair
            seedPairIndex += 2;
            // might mean there is no next index, in which case we are done
            if (seedPairIndex < seedPairs.size()) {
                currentValue = seedPairs.get(seedPairIndex);
                end = currentValue.add(seedPairs.get(seedPairIndex + 1));
            }
        }
        return result;
    }
}
