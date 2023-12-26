/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day12;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import aoc.Day;

public class Day12 implements Day {
    @Override
    public String part1(List<String> input) {
        List<Pair<List<SpringCondition>, List<Integer>>> lines = parse(input);
        BigInteger total = countPossibleArrangements(lines);
        return total.toString();
    }

    @Override
    public String part2(List<String> input) {
        List<Pair<List<SpringCondition>, List<Integer>>> lines = parse(input).stream()
                .map(pair -> {
                    List<SpringCondition> springs = new ArrayList<>();
                    List<Integer> groups = new ArrayList<>();
                    for (int i = 0; i < 5; i += 1) {
                        springs.addAll(pair.getLeft());
                        groups.addAll(pair.getRight());
                    }
                    return Pair.of(springs, groups);
                }).collect(Collectors.toList());
        BigInteger total = countPossibleArrangements(lines);
        return total.toString();
    }

    private List<Pair<List<SpringCondition>, List<Integer>>> parse(List<String> input) {
        return input.stream().map(this::parseLine).collect(Collectors.toList());
    }

    private Pair<List<SpringCondition>, List<Integer>> parseLine(String line) {
        String[] parts = line.split(" ");
        List<SpringCondition> springs = parts[0].chars().mapToObj(SpringCondition::fromChar).collect(Collectors.toList());
        List<Integer> brokenGroups = parts.length < 2 ? List.of() :
                Arrays.stream(parts[1].split(",")).map(Integer::parseInt).collect(Collectors.toList());
        return Pair.of(springs, brokenGroups);
    }

    private BigInteger countPossibleArrangements(List<Pair<List<SpringCondition>, List<Integer>>> lines) {
        return lines.parallelStream().map(this::countPossibleArrangements).reduce(BigInteger.ZERO, BigInteger::add);
    }

    private BigInteger countPossibleArrangements(Pair<List<SpringCondition>, List<Integer>> pair) {
        List<SpringCondition> springs = pair.getLeft();
        List<Integer> brokenGroups = pair.getRight();

        // one extra for brokenGroups, in the possibility where there are none left
        BigInteger[][] dp = new BigInteger[springs.size()][brokenGroups.size() + 1];
        for (int springIndex = springs.size() - 1; springIndex >= 0; springIndex -= 1) {
            for (int brokenGroupIndex = brokenGroups.size(); brokenGroupIndex >= 0; brokenGroupIndex -= 1) {
                dp[springIndex][brokenGroupIndex] = pluginBrokenGroups(springs, springIndex, brokenGroups, brokenGroupIndex, dp);
            }
        }
        BigInteger total = dp[0][0];
        if (total.compareTo(BigInteger.valueOf(1_000_000_000)) > 0) {
            System.out.printf("%s %s %d\n", springs.stream().map(Object::toString).collect(Collectors.joining()),
                    brokenGroups.stream().map(Object::toString).collect(Collectors.joining(",")),
                    total);
        }
        return total;
    }

    private BigInteger pluginBrokenGroups(List<SpringCondition> springs, int springIndex, List<Integer> brokenGroups, int brokenGroupIndex, BigInteger[][] dp) {
        // base case, we have the last spring
        if (springIndex == springs.size() - 1) {
            switch(springs.get(springIndex)) {
            case Operational:
                if (brokenGroupIndex >= brokenGroups.size()) {
                    // if no broken groups left, yay!
                    return BigInteger.ONE;
                } else {
                    // we must use another broken group, but there are no more damaged gears
                    return BigInteger.ZERO;
                }
            case Damaged:
                // if no more broken groups left, or there is but it requires more than 1 gear, no solution
                if (brokenGroupIndex == brokenGroups.size() - 1 && brokenGroups.get(brokenGroupIndex) == 1) {
                    return BigInteger.ONE;
                } else {
                    return BigInteger.ZERO;
                }
            case Unknown:
                // one solution if there is no group left, or a single group with size 1 left
                if (brokenGroupIndex >= brokenGroups.size()
                        || (brokenGroupIndex == brokenGroups.size() - 1 && brokenGroups.get(brokenGroupIndex) == 1)) {
                    return BigInteger.ONE;
                } else {
                    return BigInteger.ZERO;
                }
            }
        }

        // we either use a broken group here, or we don't
        switch (springs.get(springIndex)) {
        case Operational:
            // we don't
            return dp[springIndex + 1][brokenGroupIndex];
        case Damaged:
            return pluginBrokenGroup(springs, springIndex, brokenGroups, brokenGroupIndex, dp);
        case Unknown:
            // we may or may not use a broken group
            BigInteger usingBrokenGroup = pluginBrokenGroup(springs, springIndex, brokenGroups, brokenGroupIndex, dp);
            BigInteger notUsingBrokenGroup = dp[springIndex + 1][brokenGroupIndex];
            return usingBrokenGroup.add(notUsingBrokenGroup);
        }
        throw new IllegalStateException("to satisfy compiler");
    }

    // assume we must use a broken group now
    public BigInteger pluginBrokenGroup(List<SpringCondition> springs, int springIndex, List<Integer> brokenGroups, int brokenGroupIndex, BigInteger[][] dp) {
        // no more broken groups, or the size of the broken group is bigger than remaining spring slots
        if (brokenGroupIndex >= brokenGroups.size() || brokenGroups.get(brokenGroupIndex) + springIndex > springs.size()) {
            return BigInteger.ZERO;
        }
        Integer brokenGroup = brokenGroups.get(brokenGroupIndex);
        int nextIndex = springIndex + brokenGroup;
        if (nextIndex < springs.size() && springs.get(nextIndex) == SpringCondition.Damaged) {
            // the spring following the broken group must be nonexistent or operational
            // check this first since it is O(1) < O(groupSize)
            return BigInteger.ZERO;
        }
        for (SpringCondition type : springs.subList(springIndex + 1, nextIndex)) {
            if (type == SpringCondition.Operational) {
                // if we cannot use the broken group, then we are done. zero possibilities
                return BigInteger.ZERO;
            }
        }

        if (nextIndex + 1 >= springs.size()) {
            if (brokenGroupIndex + 1 >= brokenGroups.size()) {
                // if we are done, and there are no more groups, we are done
                return BigInteger.ONE;
            } else {
                // if we are done and there are remaining broken groups, no solution
                return BigInteger.ZERO;
            }
        } else {
            return dp[nextIndex + 1][brokenGroupIndex + 1];
        }
    }
}
