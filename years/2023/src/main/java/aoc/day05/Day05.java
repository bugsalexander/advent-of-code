/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day05;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import aoc.Day;

public class Day05 implements Day {

    Map<SeedRequirementType, Mapping> maps;

    @Override
    public String part1(List<String> input) {
        // first, chunk the input into separate "groups". each group should be separated by an empty line
        List<List<String>> groups = getGroups(input);

        // should only be one line. start after "seeds: "
        List<BigInteger> seeds = parseNumbers(groups.remove(0).get(0).substring(7));
        maps = groups.stream().map(Mapping::fromString)
                .collect(Collectors.toMap(Mapping::getSourceType, Function.identity()));
        return computeMinLocation(seeds.iterator()).toString();
    }

    @Override
    public String part2(List<String> input) {
        List<List<String>> groups = getGroups(input);

        // should only be one line. start after "seeds: "
        List<BigInteger> seedRanges = parseNumbers(groups.remove(0).get(0).substring(7));
        List<Callable<BigInteger>> tasks = new ArrayList<>();
        for (int i = 0; i < seedRanges.size(); i += 2) {
            Iterator<BigInteger> iterator = new SeedRangeIterator(seedRanges.subList(i, i + 2));
            BigInteger total = seedRanges.get(i + 1);
            tasks.add(() -> computeMinLocationAndLog(iterator, total));
        }

        maps = groups.stream().map(Mapping::fromString)
                .collect(Collectors.toMap(Mapping::getSourceType, Function.identity()));

        ExecutorService executor = Executors.newWorkStealingPool();
        CompletionService<BigInteger> completionService = new ExecutorCompletionService<>(executor);
        tasks.forEach(completionService::submit);

        int done = 0;
        BigInteger min;
        try {
            min = completionService.take().get();
            done += 1;
            while (done < tasks.size()) {
                min = min.min(completionService.take().get());
                done += 1;
                System.out.printf("tasks completed: %s / %s\n", done, tasks.size());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return min.toString();
    }

    private BigInteger computeMinLocationAndLog(Iterator<BigInteger> seeds, BigInteger total) {
        BigInteger[] done = { BigInteger.ZERO };

        // periodically report progress
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.printf("%s / %s\n", done[0], total);
            }
        }, 10 * 1000L, 10 * 1000L);

        BigInteger min = computeMinLocationForSeed(seeds.next());
        done[0] = done[0].add(BigInteger.ONE);
        while (seeds.hasNext()) {
            min = min.min(computeMinLocationForSeed(seeds.next()));
            done[0] = done[0].add(BigInteger.ONE);
        }

        timer.cancel();
        return min;
    }
    private BigInteger computeMinLocation(Iterator<BigInteger> seeds) {
        BigInteger runningMin = computeMinLocationForSeed(seeds.next());
        while (seeds.hasNext()) {
            BigInteger current = computeMinLocationForSeed(seeds.next());
            runningMin = runningMin.min(current);
        }

        return runningMin;
    }

    private BigInteger computeMinLocationForSeed(BigInteger seed) {
        TypeValuePair pair = new TypeValuePair(SeedRequirementType.Seed, seed);
        return mapToLocation(pair);
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

    private BigInteger mapToLocation(TypeValuePair startPair) {
        TypeValuePair currentPair = startPair;
        while (currentPair.getType() != SeedRequirementType.Location) {
            Mapping mapping = maps.get(currentPair.getType());
            BigInteger newValue = mapping.mapSourceToDestination(currentPair.getValue());
            SeedRequirementType newType = mapping.getDestinationType();
            currentPair = new TypeValuePair(newType, newValue);
        }

        return currentPair.getValue();
    }

    public static List<BigInteger> parseNumbers(String s) {
        return Arrays.stream(s.split(" ")).map(BigInteger::new).collect(Collectors.toList());
    }
}
