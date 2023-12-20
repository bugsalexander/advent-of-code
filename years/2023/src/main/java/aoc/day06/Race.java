/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day06;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class Race {
    private BigInteger time;
    private BigInteger recordDistance;

    public Race(BigInteger time, BigInteger recordDistance) {
        this.time = time;
        this.recordDistance = recordDistance;
    }

    public BigInteger getTime() {
        return time;
    }

    public Race setTime(BigInteger time) {
        this.time = time;
        return this;
    }

    public BigInteger getRecordDistance() {
        return recordDistance;
    }

    public Race setRecordDistance(BigInteger recordDistance) {
        this.recordDistance = recordDistance;
        return this;
    }

    public BigInteger getNumberOfWaysToBeatRecord() {
        // d(c) = (T - c) * c = Tc - c^2
        // d(c) = -c^2 + Tc
        // find c when D = recordDistance
        // 0 = -c^2 + Tc - D

        // solve
        BigDecimal a = BigDecimal.valueOf(-1);
        BigDecimal b = new BigDecimal(getTime());
        BigDecimal c = new BigDecimal(getRecordDistance().negate());

        BigDecimal discriminant = b.multiply(b).subtract(BigDecimal.valueOf(4).multiply(a).multiply(c));
        if (discriminant.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("the result would be non-real");
        }

        // keep 10 digits of precision after decimal should be good
        BigDecimal d1 = discriminant.sqrt(roundingContextFor(discriminant));

        // these are most likely not integers, but we need integer answers, so floor/ceil them
        BigDecimal x1 = divide(b.negate().subtract(d1), BigDecimal.valueOf(2).multiply(a));
        BigDecimal x2 = divide(b.negate().add(d1), BigDecimal.valueOf(2).multiply(a));

        BigInteger start = x1.min(x2).setScale(0, RoundingMode.CEILING).toBigIntegerExact();
        BigInteger end = x1.max(x2).setScale(0, RoundingMode.FLOOR).toBigIntegerExact();

        // it's possible that these produce the result of the best distance. in which case increase/decrease by 1
        if (calcDistance(start).compareTo(getRecordDistance()) == 0) {
            start = start.add(BigInteger.ONE);
        }
        if (calcDistance(end).compareTo(getRecordDistance()) == 0) {
            end = end.subtract(BigInteger.ONE);
        }


        // total solutions is max of 0 and the diff
        return BigInteger.ZERO.max(end.subtract(start).add(BigInteger.ONE));
    }

    private BigInteger calcDistance(BigInteger c) {
        // d(c) = (T - c) * c
        return getTime().subtract(c).multiply(c);
    }

    private BigDecimal divide(BigDecimal num, BigDecimal denom) {
        return num.divide(denom, roundingContextFor(num));
    }

    private MathContext roundingContextFor(BigDecimal d) {
        // keep 10 digits of precision after decimal should be good
        return new MathContext(d.precision() + 10, RoundingMode.HALF_UP);
    }
}
