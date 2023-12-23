/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day09;

import aoc.Day;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day09 implements Day {
    @Override
    public String part1(List<String> input) {
        List<List<Integer>> histories = parseHistories(input);
        BigInteger total = histories.stream()
                .map(this::buildDiffs)
                .map(diffs -> BigInteger.valueOf(this.calculateNextValue(diffs)))
                .reduce(BigInteger.ZERO, BigInteger::add);

        return total.toString();
    }

    @Override
    public String part2(List<String> input) {
        List<List<Integer>> histories = parseHistories(input);
        BigInteger total = histories.stream()
                .map(this::buildDiffs)
                .map(diffs -> BigInteger.valueOf(this.calculatePrevValue(diffs)))
                .reduce(BigInteger.ZERO, BigInteger::add);

        return total.toString();
    }

    private List<List<Integer>> parseHistories(List<String> input) {
        return input.stream()
                .map(line -> Arrays.stream(line.split(" ")).map(Integer::parseInt).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private List<List<Integer>> buildDiffs(List<Integer> history) {
        List<List<Integer>> diffs = new ArrayList<>();
        diffs.add(history);
        int depth = 0;
        while (!diffs.get(depth).stream().allMatch(n -> n == 0)) {
            List<Integer> currentLevel = diffs.get(depth);
            // the next level is one smaller, plus one (for walking back up diff)
            List<Integer> nextLevel = new ArrayList<>(currentLevel.size());
            for (int i = 0; i < currentLevel.size() - 1; i += 1) {
                int step = currentLevel.get(i + 1) - currentLevel.get(i);
                nextLevel.add(step);
            }
            diffs.add(nextLevel);
            depth += 1;
        }
        return diffs;
    }

    private int calculateNextValue(List<List<Integer>> diffs) {
        // add a zero
        diffs.get(diffs.size() - 1).add(0);
        // walk back up and calculate the next numbers
        for (int d = diffs.size() - 2; d >= 0; d -= 1) {
            List<Integer> prevLevel = diffs.get(d + 1);
            List<Integer> currentLevel = diffs.get(d);
            currentLevel.add(prevLevel.get(prevLevel.size() - 1) + currentLevel.get(currentLevel.size() - 1));
        }

        // the last #
        return diffs.get(0).get(diffs.get(0).size() - 1);
    }

    private int calculatePrevValue(List<List<Integer>> diffs) {
        // add a zero. we are calculating the prev value, but storing it at the end of the list lmao (shifting is slow)
        diffs.get(diffs.size() - 1).add(0);
        // walk back up and calculate the next numbers
        for (int d = diffs.size() - 1; d >= 1; d -= 1) {
            List<Integer> prevLevel = diffs.get(d - 1);
            List<Integer> currentLevel = diffs.get(d);
            prevLevel.add(prevLevel.get(0) - currentLevel.get(currentLevel.size() - 1));
        }

        // the last #
        return diffs.get(0).get(diffs.get(0).size() - 1);
    }
}
