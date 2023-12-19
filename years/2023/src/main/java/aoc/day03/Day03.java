/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day03;

import aoc.Day;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day03 implements Day {
    @Override
    public String part1(List<String> input) {
        char[][] grid = input.stream().map(String::toCharArray).toArray(char[][]::new);

        // every number that is adjacent to a symbol that is not "." is tallied
        // strategy: go through left to right, top down. when we encounter a #:
        // 1. get the number
        // 2. go all around it to see if there are symbols bordering it
        // 3. keep track of a set of coordinates to track numbers that are "seen"

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
                    if (isBorderedBySymbol(row, found, grid)) {
                        total += found.getValue();
                    }
                }
            }
        }

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

    private boolean isBorderedBySymbol(int row, NumberIndexRange found, char[][] grid) {
        int left = found.getStartIndex() - 1;
        int right = found.getEndIndex() + 1;
        int top = row - 1;
        int bot = row + 1;

        // check the top row
        if (0 <= top) {
            for (int i = Math.max(left, 0); i <= Math.min(right, grid[top].length - 1); i += 1) {
                if (isSymbol(top, i, grid)) {
                    return true;
                }
            }
        }
        // check the left and right
        if (0 <= left) {
            if (isSymbol(row, left, grid)) {
                return true;
            }
        }
        if (right < grid[row].length) {
            if (isSymbol(row, right, grid)) {
                return true;
            }
        }
        // check the bottom row
        if (bot < grid.length) {
            for (int i = Math.max(left, 0); i <= Math.min(right, grid[bot].length - 1); i += 1) {
                if (isSymbol(bot, i, grid)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isSymbol(int row, int col, char[][] grid) {
        // if not "." and is not digit, must be a symbol
        return grid[row][col] != '.' && !Character.isDigit(grid[row][col]);
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }
}
