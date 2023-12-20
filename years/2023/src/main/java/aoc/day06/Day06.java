/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day06;

import aoc.Day;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day06 implements Day {
    @Override
    public String part1(List<String> input) {
        List<Integer> times = parseLine(input.get(0));
        List<Integer> distances = parseLine(input.get(1));
        List<Race> races = new ArrayList<>(times.size());
        for (int i = 0; i < times.size(); i += 1) {
            races.add(new Race(BigInteger.valueOf(times.get(i)), BigInteger.valueOf(distances.get(i))));
        }

        // each race lasts T time, and records the furthest distance traveled d
        // to win, we need to go further than the record

        // hold button to "charge" boat, and then release to let boat "go"

        // for each millisecond held, it increases the speed by that much
        // boat will travel at constant speed for the remaining time of the race

        // distance can be modeled by the following function, where c is time spent charging
        // d(c) = (T - c) * c

        // determine # of ways to beat each race. multiply those all together for result.
        BigInteger total = races.stream()
                .map(Race::getNumberOfWaysToBeatRecord)
                .reduce(BigInteger.ONE, BigInteger::multiply);

        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        BigInteger time = fixKerning(input.get(0));
        BigInteger distance = fixKerning(input.get(1));
        Race race = new Race(time, distance);

        BigInteger total = race.getNumberOfWaysToBeatRecord();
        return String.valueOf(total);
    }

    private List<Integer> parseLine(String line) {
        return Arrays.stream(line.split(" "))
                .filter(s -> !s.isEmpty())
                .skip(1L)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private BigInteger fixKerning(String line) {
        String r = Arrays.stream(line.split(" "))
                .filter(s -> !s.isEmpty())
                .skip(1L)
                .collect(Collectors.joining());
        return new BigInteger(r);
    }
}
