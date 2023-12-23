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
        List<List<Integer>> histories = input.stream()
                .map(line -> Arrays.stream(line.split(" ")).map(Integer::parseInt).collect(Collectors.toList()))
                .collect(Collectors.toList());
        BigInteger total = histories.stream()
                .map(h -> BigInteger.valueOf(calculateNextValue(h)))
                .reduce(BigInteger.ZERO, BigInteger::add);

        return total.toString();
    }

    @Override
    public String part2(List<String> input) {

        return null;
    }

    private int calculateNextValue(List<Integer> h) {
        List<List<Integer>> diffs = new ArrayList<>();
        diffs.add(h);
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

        // add a zero
        diffs.get(depth).add(0);
        // walk back up and calculate the next numbers
        for (int d = depth - 1; d >= 0; d -= 1) {
            List<Integer> prevLevel = diffs.get(d + 1);
            List<Integer> currentLevel = diffs.get(d);
            currentLevel.add(prevLevel.get(prevLevel.size() - 1) + currentLevel.get(currentLevel.size() - 1));
        }

        // the last #
        return diffs.get(0).get(diffs.get(0).size() - 1);
    }
}
