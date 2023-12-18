/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day02;

import aoc.Day;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day02 implements Day {

    @Override
    public String part1(List<String> input) {
        int total = input.stream().mapToInt(line -> {
            String[] idAndRecordParts = line.split(":\\W*");
            int id = Integer.parseInt(idAndRecordParts[0].substring(5));
            // each should look like "blue 1, red 2"
            String[] pullParts = idAndRecordParts[1].split("\\W*;\\W*");

            // in the order: red | blue | green
            int[][] counts = new int[pullParts.length][3];
            for (int i = 0; i < pullParts.length; i += 1) {
                String pull = pullParts[i];
                // each should look like "blue 2"
                String[] colorCounts = pull.split("\\W*,\\W*");
                for (String colorCount : colorCounts) {
                    // should be ["blue", "2"]
                    String[] colorAndCount = colorCount.split(" ");
                    int count = Integer.parseInt(colorAndCount[0]);
                    Color color = Color.fromString(colorAndCount[1]);
                    counts[i][Objects.requireNonNull(color).getIndex()] = count;
                }
            }

            // this game is valid if every pull does not have colors which exceed their max counts
            for (int[] count : counts) {
                for (Color color : Color.values()) {
                    // the pull is invalid if any color has count > its max
                    if (count[color.getIndex()] > color.getMaxCount()) {
                        return 0;
                    }
                }
            }

            return id;
        }).sum();
        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }
}
