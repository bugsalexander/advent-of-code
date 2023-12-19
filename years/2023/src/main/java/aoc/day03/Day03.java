/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day03;

import aoc.Day;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day03 implements Day {
    @Override
    public String part1(List<String> input) {
        // every number that is adjacent to a symbol that is not "." is tallied
        // strategy: go through left to right, top down. when we encounter a #:
        // 1. get the number
        // 2. go all around it to see if there are symbols bordering it
        // 3. keep track of a set of coordinates to track numbers that are "seen"

        char[][] grid = input.stream().map(String::toCharArray).toArray(char[][]::new);
        // [list-of [list-of pair]] - each pair is a range for the "x"
        List<List<NumberIndexRange>> seen = IntStream.range(0, input.size())
                .mapToObj(_i -> new ArrayList<NumberIndexRange>())
                .collect(Collectors.toList());

        int total = 0;
        for (int row = 0; row < grid.length; row += 1) {
            for (int col = 0; col < grid[0].length; col += 1) {
                if (Character.isDigit(grid[row][col]) && !hasSeen(row, col, seen)) {
                    NumberIndexRange found = getNumberIndexRange(row, col, grid);
                    seen.get(row).add(found);
                    // hacky way to escape the stack
                    try {
                        findNeighbors(row, found, grid, this::isSymbol, coordinate -> {
                            if (coordinate.isPresent()) {
                                throw coordinate.get();
                            }
                        });
                    } catch (Coordinate coordinate) {
                        // we found a neighboring symbol
                        total += found.getValue();
                    }
                }
            }
        }

        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        // find every "*" with exactly two numbers adjacent
        // multiply those two #s together, add to total

        char[][] grid = input.stream().map(String::toCharArray).toArray(char[][]::new);
        // every number that is adjacent to a "*" records the adjacency for the "*" coordinate
        List<List<NumberIndexRange>> seen = IntStream.range(0, input.size())
                .mapToObj(_i -> new ArrayList<NumberIndexRange>())
                .collect(Collectors.toList());

        Map<Coordinate, List<NumberIndexRange>> gearNeighbors = new HashMap<>();
        for (int row = 0; row < grid.length; row += 1) {
            for (int col = 0; col < grid[0].length; col += 1) {
                if (Character.isDigit(grid[row][col]) && !hasSeen(row, col, seen)) {
                    NumberIndexRange found = getNumberIndexRange(row, col, grid);
                    seen.get(row).add(found);
                    List<Coordinate> neighboringGears = new ArrayList<>();
                    findNeighbors(row, found, grid, this::isGear, coordinate -> coordinate.ifPresent(neighboringGears::add));
                    for (Coordinate gear : neighboringGears) {
                        gearNeighbors.compute(gear, (g, v) -> {
                            List<NumberIndexRange> numberNeighbors = v != null ? v : new ArrayList<>();
                            numberNeighbors.add(found);
                            return numberNeighbors;
                        });
                    }
                }
            }
        }

        int total = gearNeighbors.entrySet().stream().map(Map.Entry::getValue).mapToInt(neighbors -> {
            if (neighbors.size() == 2) {
                return neighbors.get(0).getValue() * neighbors.get(1).getValue();
            }
            return 0;
        }).sum();

        return String.valueOf(total);
    }

    private boolean hasSeen(int row, int col, List<List<NumberIndexRange>> seen) {
        for (NumberIndexRange curr : seen.get(row)) {
            if (curr.getStartIndex() <= col && col <= curr.getEndIndex()) {
                return true;
            }
        }
        return false;
    }

    private NumberIndexRange getNumberIndexRange(int row, int col, char[][] grid) {
        int value = 0;
        // assume we are starting at the leftmost number
        int current = col;
        while (current < grid[row].length && Character.isDigit(grid[row][current])) {
            value = (value * 10) + Character.getNumericValue(grid[row][current]);
            current += 1;
        }

        return new NumberIndexRange(value, col, current - 1);
    }

    /**
     * this one's a doozy, so i'll add a javadoc. basically, searches neighboring tiles, left->right, top->bottom, checking if the characters
     * satisfy the predicate. if they satisfy the predicate, passes the predicate to the consumer.
     */
    private void findNeighbors(int row, NumberIndexRange found, char[][] grid, GridPredicate predicate, Consumer<Optional<Coordinate>> consumer) {
        int left = found.getStartIndex() - 1;
        int right = found.getEndIndex() + 1;
        int top = row - 1;
        int bot = row + 1;

        // check the top row
        if (0 <= top) {
            for (int i = Math.max(left, 0); i <= Math.min(right, grid[top].length - 1); i += 1) {
                if (predicate.test(top, i, grid)) {
                    consumer.accept(Optional.of(new Coordinate(top, i)));
                }
            }
        }
        // check the left and right
        if (0 <= left) {
            if (predicate.test(row, left, grid)) {
                consumer.accept(Optional.of(new Coordinate(row, left)));
            }
        }
        if (right < grid[row].length) {
            if (predicate.test(row, right, grid)) {
                consumer.accept(Optional.of(new Coordinate(row, right)));
            }
        }
        // check the bottom row
        if (bot < grid.length) {
            for (int i = Math.max(left, 0); i <= Math.min(right, grid[bot].length - 1); i += 1) {
                if (predicate.test(bot, i, grid)) {
                    consumer.accept(Optional.of(new Coordinate(bot, i)));
                }
            }
        }

        consumer.accept(Optional.empty());
    }

    private boolean isSymbol(int row, int col, char[][] grid) {
        // if not "." and is not digit, must be a symbol
        return grid[row][col] != '.' && !Character.isDigit(grid[row][col]);
    }

    private boolean isGear(int row, int col, char[][] grid) {
        return grid[row][col] == '*';
    }
}
