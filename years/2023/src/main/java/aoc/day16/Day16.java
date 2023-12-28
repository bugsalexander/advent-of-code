/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day16;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import aoc.Day;
import aoc.util.GenericTile;

import org.apache.commons.lang3.tuple.Pair;

public class Day16 implements Day {

    private static final Map<Tile, Map<Direction, List<Direction>>> TILE_DIRECTION_MAPPING = new HashMap<>();
    static {
        Map<Direction, List<Direction>> mirror1Map = Map.of(
                Direction.Right, List.of(Direction.Up),
                Direction.Left, List.of(Direction.Down),
                Direction.Down, List.of(Direction.Left),
                Direction.Up, List.of(Direction.Right));
        TILE_DIRECTION_MAPPING.put(Tile.Mirror1, mirror1Map);

        Map<Direction, List<Direction>> mirror2Map = Map.of(
                Direction.Right, List.of(Direction.Down),
                Direction.Left, List.of(Direction.Up),
                Direction.Down, List.of(Direction.Right),
                Direction.Up, List.of(Direction.Left));
        TILE_DIRECTION_MAPPING.put(Tile.Mirror2, mirror2Map);

        Map<Direction, List<Direction>> splitter1Map = Map.of(
                Direction.Right, List.of(Direction.Right),
                Direction.Left, List.of(Direction.Left),
                Direction.Down, List.of(Direction.Left, Direction.Right),
                Direction.Up, List.of(Direction.Left, Direction.Right));
        TILE_DIRECTION_MAPPING.put(Tile.Splitter1, splitter1Map);

        Map<Direction, List<Direction>> splitter2Map = Map.of(
                Direction.Right, List.of(Direction.Up, Direction.Down),
                Direction.Left, List.of(Direction.Up, Direction.Down),
                Direction.Down, List.of(Direction.Down),
                Direction.Up, List.of(Direction.Up));
        TILE_DIRECTION_MAPPING.put(Tile.Splitter2, splitter2Map);
    }

    @Override
    public String part1(List<String> input) {
        Tile[][] grid = input.stream()
                .map(line -> line.chars()
                        .mapToObj(c -> GenericTile.fromChar(c, Tile.class))
                        .toArray(Tile[]::new))
                .toArray(Tile[][]::new);
        // keep track of seen directions for grid
        // 3d array??? crazy??? i was crazy once
        boolean[][][] seenDirections = new boolean[grid.length][grid[0].length][Direction.values().length];

        // doesn't matter if we dfs or bfs this, just need to check seen after popping off the stack
        // ((row, col), dir)
        Stack<Pair<Pair<Integer, Integer>, Direction>> stack = new Stack<>();
        // beam enters top left, from the left
        stack.add(Pair.of(Pair.of(0, 0), Direction.Right));

        while (!stack.isEmpty()) {
            Pair<Pair<Integer, Integer>, Direction> pair = stack.pop();
            Pair<Integer, Integer> pos = pair.getLeft();
            Direction dir = pair.getRight();
            if (hasSeen(seenDirections, pos, dir)) {
                // skip if already seen this direction at this coord
                continue;
            }
            // mark as seen
            seenDirections[pos.getLeft()][pos.getRight()][dir.ordinal()] = true;

            // compute the next directions based on this boi
            List<Pair<Pair<Integer, Integer>, Direction>> nextVectors = computeNextVectors(grid, pos, dir);

            // we could check if these are seen now, but also we'll have to check them when we pop off the stack
            // so to avoid double-checking, just skip checking now
            stack.addAll(nextVectors);
        }

        // add up the squares that are energized
        int total = 0;
        for (boolean[][] row : seenDirections) {
            for (boolean[] col : row) {
                if (IntStream.range(0, Direction.values().length).anyMatch(i -> col[i])) {
                    total += 1;
                }
            }
        }
        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    private boolean hasSeen(boolean[][][] seenDirections, Pair<Integer, Integer> pos, Direction dir) {
        return seenDirections[pos.getLeft()][pos.getRight()][dir.ordinal()];
    }

    private List<Pair<Pair<Integer, Integer>, Direction>> computeNextVectors(Tile[][] grid, Pair<Integer, Integer> pos, Direction dir) {
        Tile tile = grid[pos.getLeft()][pos.getRight()];
        if (tile == Tile.Empty) {
            // direction remains the same
            return mapDirectionsToPosTuples(grid, pos, List.of(dir));
        } else {
            // so fresh and so clean
            List<Direction> nextDirections = TILE_DIRECTION_MAPPING.get(tile).get(dir);
            return mapDirectionsToPosTuples(grid, pos, nextDirections);
        }
    }

    private List<Pair<Pair<Integer, Integer>, Direction>> mapDirectionsToPosTuples(Tile[][] grid, Pair<Integer, Integer> originalPos, List<Direction> nextDirs) {
        // map originalPos + direction -> nextPos + direction
        return nextDirs.stream()
                .flatMap(dir -> getPos(grid, originalPos, dir).map(pos -> Pair.of(pos, dir)).stream())
                .collect(Collectors.toList());
    }

    private Optional<Pair<Integer, Integer>> getPos(Tile[][] grid, Pair<Integer, Integer> pos, Direction dir) {
        int row = pos.getLeft() + dir.getVector().getLeft();
        int col = pos.getRight() + dir.getVector().getRight();
        if (0 <= row && row < grid.length && 0 <= col && col < grid[0].length) {
            return Optional.of(Pair.of(row, col));
        }
        return Optional.empty();
    }
}
