/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day05;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import aoc.Day;

public class Day05 implements Day {
    @Override
    public String part1(List<String> input) {
        // first, chunk the input into separate "groups"
        // each group should be separated by an empty line
        List<List<String>> groups = new ArrayList<>();
        List<String> currentGroup = new ArrayList<>();
        for (int i = 0; i < input.size(); i += 1) {
            if (!input.get(i).isEmpty()) {
                currentGroup.add(input.get(i));
            } else {
                // we have a blank, add the current group and skip this one
                groups.add(currentGroup);
                currentGroup = new ArrayList<>();
            }
        }
        // add the last group
        groups.add(currentGroup);
        if (groups.size() != 8) {
            throw new IllegalStateException("should have 8 groups, but instead had " + groups.size());
        }

        // should only be one line. start after "seeds: "
        List<Integer> seeds = parseNumbers(groups.remove(0).get(0).substring(7));
        Map<SeedRequirementType, Mapping> maps = groups.stream().map(Mapping::fromString)
                .collect(Collectors.toMap(Mapping::getSourceType, Function.identity()));

        int lowestLocation = seeds.stream().mapToInt(seed -> mapToLocation(seed, SeedRequirementType.Seed, maps)).min().orElseThrow();

        return String.valueOf(lowestLocation);
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    private int mapToLocation(int currentValue, SeedRequirementType currentType, Map<SeedRequirementType, Mapping> maps) {
        if (currentType == SeedRequirementType.Location) {
            return currentValue;
        }

        Mapping mapping = maps.get(currentType);
        int newValue = mapping.mapSourceToDestination(currentValue);
        return mapToLocation(newValue, mapping.getDestinationType(), maps);
    }

    public static List<Integer> parseNumbers(String s) {
        return Arrays.stream(s.split(" ")).map(Integer::parseInt).collect(Collectors.toList());
    }
}
