/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day15;

import aoc.Day;
import java.math.BigInteger;
import java.util.List;

public class Day15 implements Day {
    @Override
    public String part1(List<String> input) {
        List<String> parts = List.of(input.get(0).split(","));
        BigInteger total = parts.stream()
                .map(this::computeHashAlgorithm)
                .map(BigInteger::valueOf)
                .reduce(BigInteger.ZERO, BigInteger::add);
        return total.toString();
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    private int computeHashAlgorithm(String sequence) {
        /*
        start with 0
        for each char:
        - add char to total
        - total *= 17
        - total = total % 256
         */
        return sequence.chars().reduce(0, (currentValue, c) -> ((currentValue + c) * 17) % 256);
    }
}
