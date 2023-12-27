/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day14;

import aoc.Day;
import aoc.util.GenericTile;
import java.math.BigInteger;
import java.util.List;

public class Day14 implements Day {
    @Override
    public String part1(List<String> input) {
        Tile[][] grid = input.stream()
                .map(line -> line.chars()
                        .mapToObj(c -> GenericTile.fromChar(c, Tile.class))
                        .toArray(Tile[]::new))
                .toArray(Tile[][]::new);

        // shift the rocks upward
        shiftRocksVertically(grid, 1);
        return totalRockLoad(grid).toString();
    }

    @Override
    public String part2(List<String> input) {
        Tile[][] grid = input.stream()
                .map(line -> line.chars()
                        .mapToObj(c -> GenericTile.fromChar(c, Tile.class))
                        .toArray(Tile[]::new))
                .toArray(Tile[][]::new);

        int totalCycles = 1_000_000_000;
        int onePercent = totalCycles / 100;
        for (int i = 0; i < totalCycles; i += 1) {
            shiftRocksVertically(grid, -1);
            shiftRocksHorizontally(grid, -1);
            shiftRocksVertically(grid, +1);
            shiftRocksHorizontally(grid, +1);
            if (i % onePercent == 0) {
                System.out.printf("%d%%\n", i / onePercent);
            }
        }
        return totalRockLoad(grid).toString();
    }

    private void shiftRocksVertically(Tile[][] grid, int diff) {
        // shift round rocks upward until they hit either the top or a cube rock
        for (int col = 0; col < grid[0].length; col += 1) {
            // if diff is +1 start at 0, if diff is -1 start at length - 1
            int startIndex = diff == 1 ? 0 : grid.length - 1;
            // if diff is +1 end at length, if diff is -1 end at -1
            int endIndex = diff == 1 ? grid.length : -1;
            // if diff is +1 swap is -1, if diff is -1 swap at length
            int prevSwapIndex = diff == 1 ? -1 : grid.length;
            for (int row = startIndex; row != endIndex; row += diff) {
                if (grid[row][col] == Tile.RoundRock) {
                    // move upwards to the most previous empty
                    prevSwapIndex = findNextEmptySpaceVertically(grid, col, diff, prevSwapIndex, row);
                    // swapping should be the same as setting them to round/empty, but won't overwrite it with empty
                    Tile temp = grid[prevSwapIndex][col];
                    grid[prevSwapIndex][col] = grid[row][col];
                    grid[row][col] = temp;
                } else if (grid[row][col] == Tile.CubeRock) {
                    prevSwapIndex = row;
                }
            }
        }
    }

    private void shiftRocksHorizontally(Tile[][] grid, int diff) {
        for (int row = 0; row < grid.length; row += 1) {
            int startIndex = diff == 1 ? 0 : grid[0].length - 1;
            int endIndex = diff == 1 ? grid[0].length : -1;
            int prevSwapIndex = diff == 1 ? -1 : grid[0].length;
            for (int col = startIndex; col != endIndex; col += diff) {
                if (grid[row][col] == Tile.RoundRock) {
                    // move upwards to the most previous empty
                    prevSwapIndex = findNextEmptySpaceVertically(grid, row, diff, prevSwapIndex, col);
                    // swapping should be the same as setting them to round/empty, but won't overwrite it with empty
                    Tile temp = grid[row][prevSwapIndex];
                    grid[row][prevSwapIndex] = grid[row][col];
                    grid[row][col] = temp;
                } else if (grid[row][col] == Tile.CubeRock) {
                    prevSwapIndex = col;
                }
            }
        }
    }

    private BigInteger totalRockLoad(Tile[][] grid) {
        BigInteger total = BigInteger.ZERO;
        for (int row = 0; row < grid.length; row += 1) {
            for (int col = 0; col < grid[0].length; col += 1) {
                if (grid[row][col] == Tile.RoundRock) {
                    total = total.add(BigInteger.valueOf(grid.length - row));
                }
            }
        }
        return total;
    }

    private int findNextEmptySpaceVertically(Tile[][] grid, int col, int diff, int ptr, int limit) {
        for (int i = ptr + diff; i != limit; i += diff) {
            if (grid[i][col] == Tile.Empty) {
                return i;
            }
        }

        // if we didn't find a valid new one, then return the limit
        return limit;
    }

    private int findNextEmptySpaceHorizontally(Tile[][] grid, int row, int diff, int ptr, int limit) {
        for (int i = ptr + diff; i != limit; i += diff) {
            if (grid[row][i] == Tile.Empty) {
                return i;
            }
        }

        // if we didn't find a valid new one, then return the limit
        return limit;
    }
}
