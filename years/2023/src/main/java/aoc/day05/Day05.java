/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day05;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import aoc.Day;

public class Day05 implements Day {

    private HashMap<TypeValuePair, TypeValuePair> lookupTable = new HashMap<>();

    @Override
    public String part1(List<String> input) {
        // first, chunk the input into separate "groups". each group should be separated by an empty line
        List<List<String>> groups = getGroups(input);

        // should only be one line. start after "seeds: "
        List<BigInteger> seeds = parseNumbers(groups.remove(0).get(0).substring(7));
        return computeMinLocation(seeds.iterator(), groups).toString();
    }

    @Override
    public String part2(List<String> input) {
        List<List<String>> groups = getGroups(input);

        // should only be one line. start after "seeds: "
        List<BigInteger> seedRanges = parseNumbers(groups.remove(0).get(0).substring(7));
        // this actually corresponds to a list of pairs, where the first is the start, and the second is the range
        Iterator<BigInteger> seedRangeIterator = new SeedRangeIterator(seedRanges);

        return computeMinLocation(seedRangeIterator, groups).toString();
    }

    private BigInteger computeMinLocation(Iterator<BigInteger> seeds, List<List<String>> groups) {
        Map<SeedRequirementType, Mapping> maps = groups.stream().map(Mapping::fromString)
                .collect(Collectors.toMap(Mapping::getSourceType, Function.identity()));

        BigInteger runningMin = computeMinLocation(seeds.next(), maps);
        while (seeds.hasNext()) {
            BigInteger current = computeMinLocation(seeds.next(), maps);
            runningMin = runningMin.min(current);
        }

        return runningMin;
    }

    private BigInteger computeMinLocation(BigInteger seed, Map<SeedRequirementType, Mapping> maps) {
        TypeValuePair pair = new TypeValuePair(SeedRequirementType.Seed, seed);
        return mapToLocation(pair, maps);
    }

    private static List<List<String>> getGroups(List<String> input) {
        List<List<String>> groups = new ArrayList<>();
        List<String> currentGroup = new ArrayList<>();
        for (String s : input) {
            if (!s.isEmpty()) {
                currentGroup.add(s);
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
        return groups;
    }

    private BigInteger mapToLocation(TypeValuePair startPair, Map<SeedRequirementType, Mapping> maps) {
        List<TypeValuePair> pairsSeen = new ArrayList<>();
        pairsSeen.add(startPair);
        TypeValuePair currentPair = startPair;
        while (currentPair.getType() != SeedRequirementType.Location) {
            if (lookupTable.containsKey(currentPair)) {
                currentPair = lookupTable.get(currentPair);
            } else {
                Mapping mapping = maps.get(currentPair.getType());
                BigInteger newValue = mapping.mapSourceToDestination(currentPair.getValue());
                SeedRequirementType newType = mapping.getDestinationType();
                currentPair = new TypeValuePair(newType, newValue);
            }
            pairsSeen.add(currentPair);
        }

        TypeValuePair finalResult = currentPair;
        for (TypeValuePair pair : pairsSeen) {
            // map them all to the final pair
            if (!finalResult.equals(lookupTable.get(pair)) && !finalResult.equals(pair)) {
                lookupTable.put(pair, finalResult);
            }
        }

        return currentPair.getValue();
    }

    public static List<BigInteger> parseNumbers(String s) {
        return Arrays.stream(s.split(" ")).map(BigInteger::new).collect(Collectors.toList());
    }
}
