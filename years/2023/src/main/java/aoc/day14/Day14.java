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
        shiftRocksUpward(grid);
        return totalRockLoad(grid).toString();
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    private void shiftRocksUpward(Tile[][] grid) {
        // shift round rocks upward until they hit either the top or a cube rock
        for (int col = 0; col < grid[0].length; col += 1) {
            int prevSwapIndex = -1;
            for (int row = 0; row < grid.length; row += 1) {
                if (grid[row][col] == Tile.RoundRock) {
                    // move upwards to the most previous empty
                    prevSwapIndex = findNextEmptySpace(grid, col, prevSwapIndex, row);
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

    private int findNextEmptySpace(Tile[][] grid, int col, int ptr, int limit) {
        for (int i = ptr + 1; i < limit; i += 1) {
            if (grid[i][col] == Tile.Empty) {
                return i;
            }
        }

        // if we didn't find a valid new one, then return the limit
        return limit;
    }
}
