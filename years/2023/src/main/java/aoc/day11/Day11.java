/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import aoc.Day;
import aoc.util.Coord;

public class Day11 implements Day {
    @Override
    public String part1(List<String> input) {
        // first, find all columns and rows that have no galaxies
        List<Coord> galaxies = new ArrayList<>();
        TileType[][] grid = new TileType[input.size()][input.get(0).length()];
        boolean[] isRowEmpty = new boolean[input.size()];
        boolean[] isColEmpty = new boolean[input.get(0).length()];
        Arrays.fill(isRowEmpty, true);
        Arrays.fill(isColEmpty, true);
        for (int row = 0; row < input.size(); row += 1) {
            for (int col = 0; col < input.get(0).length(); col += 1) {
                TileType t = TileType.fromChar(input.get(row).charAt(col));
                if (t == TileType.Galaxy) {
                    galaxies.add(new Coord(row, col));
                }
                grid[row][col] = t;
                isRowEmpty[row] = isRowEmpty[row] && t == TileType.EmptySpace;
                isColEmpty[col] = isColEmpty[col] && t == TileType.EmptySpace;
            }
        }
        // increment row/col of all pairs that come after empty row/cols
        int[] emptyRows = keepTrueIndices(isRowEmpty);
        int[] emptyCols = keepTrueIndices(isColEmpty);
        List<Coord> expandedGalaxies = galaxies.stream()
                .map(galaxy -> {
                    long rowSpace = Arrays.stream(emptyRows).filter(i -> i < galaxy.getRow()).count();
                    long colSpace = Arrays.stream(emptyCols).filter(i -> i < galaxy.getCol()).count();
                    return new Coord(galaxy.getRow() + (int) rowSpace, galaxy.getCol() + (int) colSpace);
                }).collect(Collectors.toList());

        // get the min length between all unique pairs of galaxies
        int total = 0;
        for (int first = 0; first < expandedGalaxies.size(); first += 1) {
            for (int second = first + 1; second < expandedGalaxies.size(); second += 1) {
                Coord c1 = expandedGalaxies.get(first);
                Coord c2 = expandedGalaxies.get(second);
                int distance = Math.abs(c1.getRow() - c2.getRow()) + Math.abs(c1.getCol() - c2.getCol());
                total += distance;
            }
        }
        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    public int[] keepTrueIndices(boolean[] indicesToKeep) {
        return IntStream.range(0, indicesToKeep.length)
                .flatMap(i -> indicesToKeep[i] ? IntStream.of(i) : IntStream.empty())
                .toArray();
    }
}
