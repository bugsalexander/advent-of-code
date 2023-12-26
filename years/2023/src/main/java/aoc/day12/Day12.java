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
        List<Pair<List<SpringCondition>, List<Integer>>> lines = parse(input);
        BigInteger total = countPossibleArrangements(lines);
        return total.toString();
    }

    @Override
    public String part2(List<String> input) {
        return null;
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
        return lines.stream().map(this::countPossibleArrangements).reduce(BigInteger.ZERO, BigInteger::add);
    }

    private BigInteger countPossibleArrangements(Pair<List<SpringCondition>, List<Integer>> pair) {
        List<SpringCondition> springs = pair.getLeft();
        List<Integer> brokenGroups = pair.getRight();

        int total = pluginBrokenGroups(springs, 0, brokenGroups, 0);
        System.out.println(total);
        return BigInteger.valueOf(total);
    }

    private int pluginBrokenGroups(List<SpringCondition> springs, int springIndex, List<Integer> brokenGroups, int brokenGroupIndex) {
        // base case, we have the last spring
        if (springIndex == springs.size() - 1) {
            switch(springs.get(springIndex)) {
            case Operational:
                if (brokenGroupIndex >= brokenGroups.size()) {
                    // if no broken groups left, yay!
                    return 1;
                } else {
                    // we must use another broken group, but there are no more damaged gears
                    return 0;
                }
            case Damaged:
                // if no more broken groups left, or there is but it requires more than 1 gear, no solution
                if (brokenGroupIndex >= brokenGroups.size() || brokenGroups.get(brokenGroupIndex) != 1) {
                    return 0;
                } else {
                    return 1;
                }
            case Unknown:
                // one solution if there is no group left, or a single group with size 1 left
                if (brokenGroupIndex >= brokenGroups.size()
                        || (brokenGroupIndex == brokenGroups.size() - 1 && brokenGroups.get(brokenGroupIndex) == 1)) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }

        // we either use a broken group here, or we don't
        switch (springs.get(springIndex)) {
        case Operational:
            // we don't
            return pluginBrokenGroups(springs, springIndex + 1, brokenGroups, brokenGroupIndex);
        case Damaged:
            return pluginBrokenGroup(springs, springIndex, brokenGroups, brokenGroupIndex);
        case Unknown:
            // we may or may not use a broken group
            int usingBrokenGroup = pluginBrokenGroup(springs, springIndex, brokenGroups, brokenGroupIndex);
            int notUsingBrokenGroup = pluginBrokenGroups(springs, springIndex + 1, brokenGroups, brokenGroupIndex);
            return usingBrokenGroup + notUsingBrokenGroup;
        }
        throw new IllegalStateException("to satisfy compiler");
    }

    // assume we must use a broken group now
    public int pluginBrokenGroup(List<SpringCondition> springs, int springIndex, List<Integer> brokenGroups, int brokenGroupIndex) {
        // no more broken groups, or the size of the broken group is bigger than remaining spring slots
        if (brokenGroupIndex >= brokenGroups.size() || brokenGroups.get(brokenGroupIndex) + springIndex > springs.size()) {
            return 0;
        }
        Integer brokenGroup = brokenGroups.get(brokenGroupIndex);
        int nextIndex = springIndex + brokenGroup;
        if (nextIndex < springs.size() && springs.get(nextIndex) == SpringCondition.Damaged) {
            // the spring following the broken group must be nonexistent or operational
            // check this first since it is O(1) < O(groupSize)
            return 0;
        }
        for (SpringCondition type : springs.subList(springIndex + 1, nextIndex)) {
            if (type == SpringCondition.Operational) {
                // if we cannot use the broken group, then we are done. zero possibilities
                return 0;
            }
        }

        if (nextIndex + 1 >= springs.size()) {
            if (brokenGroupIndex + 1 >= brokenGroups.size()) {
                // if we are done, and there are no more groups, we are done
                return 1;
            } else {
                // if we are done and there are remaining broken groups, no solution
                return 0;
            }
        } else {
            return pluginBrokenGroups(springs, nextIndex + 1, brokenGroups, brokenGroupIndex + 1);
        }
    }
}
