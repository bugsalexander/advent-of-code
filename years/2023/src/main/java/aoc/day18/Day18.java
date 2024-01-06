/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day18;

import aoc.Day;
import aoc.day10.Day10;
import aoc.day10.Direction;
import aoc.day10.Pipe;
import aoc.day10.Tile;
import aoc.day10.TileType;
import aoc.util.GridDirection;
import aoc.util.Posn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

public class Day18 implements Day {

    private static final Map<GridDirection, Direction> DIR_MAPPING = Map.of(
            GridDirection.Left, Direction.West,
            GridDirection.Right, Direction.East,
            GridDirection.Up, Direction.North,
            GridDirection.Down, Direction.South);

    @Override
    public String part1(List<String> input) {
        List<HexColor> digPlan = parseInput(input);
        Posn current = Posn.of(0, 0);
        int minRow = current.getRow();
        int minCol = current.getCol();
        int maxRow = current.getRow();
        int maxCol = current.getCol();
        for (HexColor hexColor : digPlan) {
            int rowDiff = hexColor.getDir().getPosnDiff().getRow() * hexColor.getSteps();
            int colDiff = hexColor.getDir().getPosnDiff().getCol() * hexColor.getSteps();
            Posn next = Posn.add(current, Posn.of(rowDiff, colDiff));
            minRow = Math.min(minRow, next.getRow());
            minCol = Math.min(minCol, next.getCol());
            maxRow = Math.max(maxRow, next.getRow());
            maxCol = Math.max(maxCol, next.getCol());
            current = next;
        }
        if (!Posn.of(0, 0).equals(current)) {
            throw new IllegalStateException("we didn't get back to start!");
        }

        // todo: adapt the day10 part2 algorithm to work for this
        Tile[][] grid = new Tile[maxRow - minRow + 1][maxCol - minCol + 1];
        insertTiles(grid, digPlan, -minRow, -minCol);

        Tile topLeftmostTile = Arrays.stream(grid[0]).filter(t -> t.getPipe().isPresent()).findFirst().orElseThrow();

        // run the day10 part2 algorithm
        Pair<Integer, Integer> loopTilesAndInnerTiles = new Day10().countLoopTilesAndContainedTiles(topLeftmostTile, grid);
        return String.valueOf(loopTilesAndInnerTiles.getLeft() + loopTilesAndInnerTiles.getRight());
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    private List<HexColor> parseInput(List<String> input) {
        Map<Character, GridDirection> mapping = Map.of(
                'R', GridDirection.Right,
                'L', GridDirection.Left,
                'U', GridDirection.Up,
                'D', GridDirection.Down);
        return input.stream().map(line -> {
            // R 6 (#70c710)
            String[] parts = line.split(" ");
            GridDirection dir = mapping.get(parts[0].charAt(0));
            int steps = Integer.parseInt(parts[1]);
            String color = parts[2].substring(2, parts[2].length() - 1);
            return new HexColor(dir, steps, color);
        }).collect(Collectors.toList());
    }

    private void insertTiles(Tile[][] grid, List<HexColor> digPlan, int originRow, int originCol) {
        // init the grid
        for (int row = 0; row < grid.length; row += 1) {
            for (int col = 0; col < grid[0].length; col += 1) {
                grid[row][col] = new Tile(TileType.Ground, Posn.of(row, col));
            }
        }

        // save start and add forward edges, current -> next
        Posn start = Posn.of(originRow, originCol);
        Posn current = start;
        for (HexColor hexColor : digPlan) {
            for (int i = 0; i < hexColor.getSteps(); i += 1) {
                Posn next = current.add(hexColor.getDir().getPosnDiff());
                // add edges current <-> next
                addBidirectionalEdge(grid, current, next, hexColor);
                current = next;
            }
        }

        // now, add the corresponding pipe type for each tile in the loop
        setPipeTypesForGrid(grid, start);
    }

    private Tile getTile(Tile[][] grid, Posn p) {
        return grid[p.getRow()][p.getCol()];
    }

    private void addBidirectionalEdge(Tile[][] grid, Posn current, Posn next, HexColor hexColor) {
        Tile prevTile = getTile(grid, current);
        Tile nextTile = getTile(grid, next);
        Direction dir = DIR_MAPPING.get(hexColor.getDir());
        prevTile.getConnections().add(Pair.of(dir, nextTile));
        nextTile.getConnections().add(Pair.of(dir.getOpposite(), prevTile));
    }

    private void setPipeTypesForGrid(Tile[][] grid, Posn origin) {
        List<Tile> queue = List.of(getTile(grid, origin));
        HashSet<Tile> seen = new HashSet<>(queue);
        while (!queue.isEmpty()) {
            List<Tile> nextQueue = new ArrayList<>();
            for (Tile t : queue) {
                t.setTypeBasedOnConnections(t.getConnections());
                nextQueue.addAll(t.getConnections().stream()
                        .map(Pair::getRight)
                        .filter(n -> !seen.contains(n))
                        .peek(seen::add)
                        .collect(Collectors.toList()));
            }
            queue = nextQueue;
        }
    }
}
