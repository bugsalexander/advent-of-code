/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day02;

import aoc.Day;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day02 implements Day {

    @Override
    public String part1(List<String> input) {
        int total = input.stream().mapToInt(line -> {
            Game game = Game.fromString(line);
            if (game.isValid()) {
                return game.getId();
            } else {
                return 0;
            }
        }).sum();
        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        int total = input.stream().mapToInt(line -> {
            Game game = Game.fromString(line);
            HashMap<Color, Integer> minCounts = new HashMap<>();
            for (int[] pull : game.getPulls()) {
                for (Color color : Color.values()) {
                    minCounts.compute(color, (_c, value) -> {
                        int countToAdd = pull[color.getIndex()];
                        if (value != null) {
                            return Math.max(countToAdd, value);
                        } else {
                            return countToAdd;
                        }
                    });
                }
            }
            // the power is the number of cubes multiplied together
            return Arrays.stream(Color.values()).mapToInt(minCounts::get).reduce(1, (a, b) -> a * b);
        }).sum();
        return String.valueOf(total);
    }
}
