/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day16;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import aoc.Day;
import aoc.util.GridDirection;
import aoc.util.GenericTile;

import org.apache.commons.lang3.tuple.Pair;

public class Day16 implements Day {

    private static final Map<Tile, Map<GridDirection, List<GridDirection>>> TILE_DIRECTION_MAPPING = new HashMap<>();
    static {
        Map<GridDirection, List<GridDirection>> mirror1Map = Map.of(
                GridDirection.Right, List.of(GridDirection.Up),
                GridDirection.Left, List.of(GridDirection.Down),
                GridDirection.Down, List.of(GridDirection.Left),
                GridDirection.Up, List.of(GridDirection.Right));
        TILE_DIRECTION_MAPPING.put(Tile.Mirror1, mirror1Map);

        Map<GridDirection, List<GridDirection>> mirror2Map = Map.of(
                GridDirection.Right, List.of(GridDirection.Down),
                GridDirection.Left, List.of(GridDirection.Up),
                GridDirection.Down, List.of(GridDirection.Right),
                GridDirection.Up, List.of(GridDirection.Left));
        TILE_DIRECTION_MAPPING.put(Tile.Mirror2, mirror2Map);

        Map<GridDirection, List<GridDirection>> splitter1Map = Map.of(
                GridDirection.Right, List.of(GridDirection.Right),
                GridDirection.Left, List.of(GridDirection.Left),
                GridDirection.Down, List.of(GridDirection.Left, GridDirection.Right),
                GridDirection.Up, List.of(GridDirection.Left, GridDirection.Right));
        TILE_DIRECTION_MAPPING.put(Tile.Splitter1, splitter1Map);

        Map<GridDirection, List<GridDirection>> splitter2Map = Map.of(
                GridDirection.Right, List.of(GridDirection.Up, GridDirection.Down),
                GridDirection.Left, List.of(GridDirection.Up, GridDirection.Down),
                GridDirection.Down, List.of(GridDirection.Down),
                GridDirection.Up, List.of(GridDirection.Up));
        TILE_DIRECTION_MAPPING.put(Tile.Splitter2, splitter2Map);
    }

    @Override
    public String part1(List<String> input) {
        Tile[][] grid = input.stream()
                .map(line -> line.chars()
                        .mapToObj(c -> GenericTile.fromChar(c, Tile.class))
                        .toArray(Tile[]::new))
                .toArray(Tile[][]::new);
        int total = totalEnergizedTilesWithStartVector(grid, Pair.of(0, 0), GridDirection.Right);
        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        Tile[][] grid = input.stream()
                .map(line -> line.chars()
                        .mapToObj(c -> GenericTile.fromChar(c, Tile.class))
                        .toArray(Tile[]::new))
                .toArray(Tile[][]::new);
        // find the energized amount for all possible edges
        int max = 0;
        for (int row = 0; row < grid.length; row += 1) {
            max = Math.max(max, totalEnergizedTilesWithStartVector(grid, Pair.of(row, 0), GridDirection.Right));
            max = Math.max(max, totalEnergizedTilesWithStartVector(grid, Pair.of(row, grid[0].length - 1), GridDirection.Left));
        }
        for (int col = 0; col < grid[0].length; col += 1) {
            max = Math.max(max, totalEnergizedTilesWithStartVector(grid, Pair.of(0, col), GridDirection.Down));
            max = Math.max(max, totalEnergizedTilesWithStartVector(grid, Pair.of(grid.length - 1, col), GridDirection.Up));
        }
        return String.valueOf(max);
    }

    private int totalEnergizedTilesWithStartVector(Tile[][] grid, Pair<Integer, Integer> startPos, GridDirection startDir) {
        // keep track of seen directions for grid
        // 3d array??? crazy??? i was crazy once
        boolean[][][] seenDirections = new boolean[grid.length][grid[0].length][GridDirection.values().length];

        // doesn't matter if we dfs or bfs this, just need to check seen after popping off the stack
        // ((row, col), dir)
        Stack<Pair<Pair<Integer, Integer>, GridDirection>> stack = new Stack<>();
        // beam enters top left, from the left
        stack.add(Pair.of(startPos, startDir));

        while (!stack.isEmpty()) {
            Pair<Pair<Integer, Integer>, GridDirection> pair = stack.pop();
            Pair<Integer, Integer> pos = pair.getLeft();
            GridDirection dir = pair.getRight();
            if (hasSeen(seenDirections, pos, dir)) {
                // skip if already seen this direction at this coord
                continue;
            }
            // mark as seen
            seenDirections[pos.getLeft()][pos.getRight()][dir.ordinal()] = true;

            // compute the next directions based on this boi
            List<Pair<Pair<Integer, Integer>, GridDirection>> nextVectors = computeNextVectors(grid, pos, dir);

            // we could check if these are seen now, but also we'll have to check them when we pop off the stack
            // so to avoid double-checking, just skip checking now
            stack.addAll(nextVectors);
        }

        // add up the squares that are energized
        int total = 0;
        for (boolean[][] row : seenDirections) {
            for (boolean[] col : row) {
                if (IntStream.range(0, GridDirection.values().length).anyMatch(i -> col[i])) {
                    total += 1;
                }
            }
        }
        return total;
    }

    private boolean hasSeen(boolean[][][] seenDirections, Pair<Integer, Integer> pos, GridDirection dir) {
        return seenDirections[pos.getLeft()][pos.getRight()][dir.ordinal()];
    }

    private List<Pair<Pair<Integer, Integer>, GridDirection>> computeNextVectors(Tile[][] grid, Pair<Integer, Integer> pos, GridDirection dir) {
        Tile tile = grid[pos.getLeft()][pos.getRight()];
        if (tile == Tile.Empty) {
            // direction remains the same
            return mapDirectionsToPosTuples(grid, pos, List.of(dir));
        } else {
            // so fresh and so clean
            List<GridDirection> nextDirections = TILE_DIRECTION_MAPPING.get(tile).get(dir);
            return mapDirectionsToPosTuples(grid, pos, nextDirections);
        }
    }

    private List<Pair<Pair<Integer, Integer>, GridDirection>> mapDirectionsToPosTuples(Tile[][] grid, Pair<Integer, Integer> originalPos, List<GridDirection> nextDirs) {
        // map originalPos + direction -> nextPos + direction
        return nextDirs.stream()
                .flatMap(dir -> getPos(grid, originalPos, dir).map(pos -> Pair.of(pos, dir)).stream())
                .collect(Collectors.toList());
    }

    private Optional<Pair<Integer, Integer>> getPos(Tile[][] grid, Pair<Integer, Integer> pos, GridDirection dir) {
        int row = pos.getLeft() + dir.getVector().getLeft();
        int col = pos.getRight() + dir.getVector().getRight();
        if (0 <= row && row < grid.length && 0 <= col && col < grid[0].length) {
            return Optional.of(Pair.of(row, col));
        }
        return Optional.empty();
    }
}
