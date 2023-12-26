/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day11;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import aoc.Day;
import aoc.util.Coord;
import aoc.util.Pair;

public class Day11 implements Day {
    @Override
    public String part1(List<String> input) {
        // first, find all columns and rows that have no galaxies
        // increment row/col of all pairs that come after empty row/cols
        BigInteger total = filterGalaxiesAndCountEmptySpace(input, BigInteger.TWO);
        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        // increment row/col of all pairs that come after empty row/cols
        BigInteger total = filterGalaxiesAndCountEmptySpace(input, BigInteger.valueOf(1_000_000));
        return String.valueOf(total);
    }

    public BigInteger filterGalaxiesAndCountEmptySpace(List<String> input, BigInteger expandedSpacePerEmpty) {
        // first, find all columns and rows that have no galaxies
        boolean[] isRowEmpty = new boolean[input.size()];
        boolean[] isColEmpty = new boolean[input.get(0).length()];
        Arrays.fill(isRowEmpty, true);
        Arrays.fill(isColEmpty, true);
        List<Coord> galaxies = new ArrayList<>();
        for (int row = 0; row < input.size(); row += 1) {
            for (int col = 0; col < input.get(0).length(); col += 1) {
                TileType t = TileType.fromChar(input.get(row).charAt(col));
                if (t == TileType.Galaxy) {
                    galaxies.add(new Coord(row, col));
                }
                isRowEmpty[row] = isRowEmpty[row] && t == TileType.EmptySpace;
                isColEmpty[col] = isColEmpty[col] && t == TileType.EmptySpace;
            }
        }
        return expandGalaxiesAndCountShortestPathDistances(expandedSpacePerEmpty.subtract(BigInteger.ONE), galaxies, isRowEmpty, isColEmpty);
    }

    private BigInteger expandGalaxiesAndCountShortestPathDistances(BigInteger expandedSpacePerEmpty, List<Coord> galaxies, boolean[] isRowEmpty, boolean[] isColEmpty) {
        int[] emptyRows = keepTrueIndices(isRowEmpty);
        int[] emptyCols = keepTrueIndices(isColEmpty);
        List<Pair<BigInteger, BigInteger>> expandedGalaxies = galaxies.stream().map(galaxy -> {
            BigInteger rowSpace = BigInteger.valueOf(Arrays.stream(emptyRows).filter(i -> i < galaxy.getRow()).count()).multiply(expandedSpacePerEmpty);
            BigInteger colSpace = BigInteger.valueOf(Arrays.stream(emptyCols).filter(i -> i < galaxy.getCol()).count()).multiply(expandedSpacePerEmpty);
            return Pair.of(BigInteger.valueOf(galaxy.getRow()).add(rowSpace), BigInteger.valueOf(galaxy.getCol()).add(colSpace));
        }).collect(Collectors.toList());

        // get the min length between all unique pairs of galaxies
        BigInteger total = BigInteger.ZERO;
        for (int first = 0; first < expandedGalaxies.size(); first += 1) {
            for (int second = first + 1; second < expandedGalaxies.size(); second += 1) {
                Pair<BigInteger, BigInteger> c1 = expandedGalaxies.get(first);
                Pair<BigInteger, BigInteger> c2 = expandedGalaxies.get(second);
                BigInteger distance = c1.getFirst().subtract(c2.getFirst()).abs().add(c1.getSecond().subtract(c2.getSecond()).abs());
                total = total.add(distance);
            }
        }

        return total;
    }

    public int[] keepTrueIndices(boolean[] indicesToKeep) {
        return IntStream.range(0, indicesToKeep.length).flatMap(i -> indicesToKeep[i] ? IntStream.of(i) : IntStream.empty()).toArray();
    }
}
