/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day04;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import aoc.Day;

public class Day04 implements Day {

    @Override
    public String part1(List<String> input) {
        int total = input.stream().mapToInt(line -> {
            // each line has winning numbers, |, numbers i have
            String[] cardAndNumbers = line.split(": ");
            String[] numbers = cardAndNumbers[1].split(" \\| ");
            Set<Integer> winningNumbers = new HashSet<>(parseNumbers(numbers[0]));
            List<Integer> numbersIHave = parseNumbers(numbers[1]);
            int base = 1;
            for (Integer n : numbersIHave) {
                if (winningNumbers.contains(n)) {
                    base *= 2;
                }
            }
            // 1 -> 0, 2 -> 1, 4 -> 2, etc
            return base / 2;
        }).sum();
        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    private List<Integer> parseNumbers(String s) {
        return Arrays.stream(s.split(" ")).filter(ss -> !ss.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
