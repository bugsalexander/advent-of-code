/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day12;

import aoc.Day;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

public class Day12 implements Day {
    @Override
    public String part1(List<String> input) {
        List<Pair<SpringCondition[], List<Integer>>> lines = parse(input);
        BigInteger total = countPossibleArrangements(lines);
        return total.toString();
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    private List<Pair<SpringCondition[], List<Integer>>> parse(List<String> input) {
        return input.stream()
                .map(this::parseLine)
                .collect(Collectors.toList());
    }

    private Pair<SpringCondition[], List<Integer>> parseLine(String line) {
        String[] parts = line.split(" ");
        SpringCondition[] springs = parts[0].chars()
                .mapToObj(SpringCondition::fromChar)
                .toArray(SpringCondition[]::new);
        List<Integer> brokenGroups = Arrays.stream(parts[1].split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return Pair.of(springs, brokenGroups);
    }

    private BigInteger countPossibleArrangements(List<Pair<SpringCondition[], List<Integer>>> lines) {
        return lines.stream()
                .map(this::countPossibleArrangements)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    private BigInteger countPossibleArrangements(Pair<SpringCondition[], List<Integer>> pair) {
        SpringCondition[] springs = pair.getLeft();
        List<Integer> brokenGroups = pair.getRight();
        // dp[type][i] = total possible arrangements, if type = type at index = i
        int[][] dp = new int[SpringCondition.values().length][pair.getLeft().length];

        // initialize dp
        SpringCondition current = springs[0];
        if (current == SpringCondition.Unknown) {
            for (SpringCondition type : SpringCondition.values()) {
                // one potential solution so far
                dp[type.ordinal()][0] = 1;
            }
        } else {
            for (SpringCondition type : SpringCondition.values()) {
                // zero solutions if type does not match current
                dp[type.ordinal()][0] = current == type ? 1 : 0;
            }
        }

        // iterate through setting dp as we go
        for (int i = 1; i < springs.length; i += 1) {
            current = springs[i];
            for (SpringCondition type : SpringCondition.values()) {
                if (current != type) {
                    // zero solutions where the previous char is
                    dp[type.ordinal()][i] = 0;
                }
            }
        }
    }
}
