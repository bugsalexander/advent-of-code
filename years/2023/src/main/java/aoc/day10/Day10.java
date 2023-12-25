/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import aoc.Day;
import aoc.util.Pair;

import org.apache.commons.collections4.SetUtils;

public class Day10 implements Day {
    @Override
    public String part1(List<String> input) {
        // build the graph, find the loop, conduct BFS to find the furthest tile
        Tile[][] tiles = new Tile[input.size()][input.get(0).length()];
        Tile start = buildGraph(tiles, input);
        int distance = bfs(start, tiles, _t -> {});
        return String.valueOf(distance - 1);
    }

    @Override
    public String part2(List<String> input) {
        Tile[][] tiles = new Tile[input.size()][input.get(0).length()];
        Tile start = buildGraph(tiles, input);

        // all tiles in the map should have a pipe
        HashMap<Integer, List<Tile>> columnToRowTilesInLoop = new HashMap<>();
        bfs(start, tiles, tileInLoop -> {
            List<Tile> cols = columnToRowTilesInLoop.computeIfAbsent(tileInLoop.getCoord().getRow(), v -> new ArrayList<>());
            cols.add(tileInLoop);
        });

        int total = 0;
        for (List<Tile> cols : columnToRowTilesInLoop.values()) {
            // we want the spaces between "Segments"
            // start counting when we encounter an end joint: |, 7, J
            // stop counting when we encounter a start joint: |, F, L
            Set<Pipe> startPipes = Set.of(Pipe.Vertical, Pipe.BendSouthWest, Pipe.BendNorthWest);
            Set<Pipe> stopPipes = Set.of(Pipe.Vertical, Pipe.BendSouthEast, Pipe.BendNorthEast);
            Set<Pipe> significantPipes = SetUtils.union(startPipes, stopPipes);
            List<Tile> joints = cols.stream()
                    .filter(t -> significantPipes.contains(t.getPipe().orElseThrow()))
                    .sorted(Comparator.comparingInt(a -> a.getCoord().getCol()))
                    .collect(Collectors.toList());

            Tile prevSignificantTile = joints.get(0);
            boolean inside = startPipes.contains(prevSignificantTile.getPipe().orElseThrow());
            for (int i = 1; i < joints.size(); i += 1) {
                Tile t = joints.get(i);
                Pipe p = t.getPipe().orElseThrow();
                if (stopPipes.contains(p) && inside) {
                    total += t.getCoord().getCol() - prevSignificantTile.getCoord().getCol() - 1;
                    inside = false;
                    prevSignificantTile = t;
                } else if (startPipes.contains(p) && !inside) {
                    inside = true;
                    prevSignificantTile = t;
                }
            }
        }
        return String.valueOf(total);
    }

    private Tile buildGraph(Tile[][] tiles, List<String> input) {
        Tile start = null;
        for (int row = 0; row < tiles.length; row += 1) {
            TileType[] tileTypes = input.get(row).chars().mapToObj(c -> TileType.fromChar((char) c)).toArray(TileType[]::new);
            for (int col = 0; col < tiles[0].length; col += 1) {
                Tile t = new Tile(tileTypes[col], new Coord(row, col));
                if (t.getType() == TileType.Start) {
                    start = t;
                }
                tiles[row][col] = t;
            }
        }
        // set the corresponding pipe connection for start tile
        Set<Direction> ourConnections = getAttemptedConnections(Objects.requireNonNull(start), tiles)
                .stream().map(Pair::getKey).collect(Collectors.toSet());
        for (Pipe pipe : Pipe.values()) {
            Set<Direction> pipeConnections = new HashSet<>(pipe.getDirections());
            if (pipeConnections.containsAll(ourConnections) && ourConnections.containsAll(pipeConnections)) {
                start.setPipe(pipe);
                break;
            }
        }

        return start;
    }

    private int bfs(Tile start, Tile[][] grid, Consumer<Tile> tileAction) {
        HashSet<Tile> seen = new HashSet<>();
        List<Tile> queue = List.of(Objects.requireNonNull(start));
        int currentDistance = 0;
        while (!queue.isEmpty()) {
            List<Tile> nextQueue = new ArrayList<>();
            for (Tile tile : queue) {
                getAttemptedConnections(tile, grid).stream()
                        .map(Pair::getValue)
                        .filter(t -> t != null && !seen.contains(t)).forEach(t -> {
                            nextQueue.add(t);
                            seen.add(t);
                            tileAction.accept(t);
                        });
            }
            queue = nextQueue;
            currentDistance += 1;
        }
        return currentDistance;
    }

    private List<Pair<Direction, Tile>> getAttemptedConnections(Tile t, Tile[][] grid) {
        List<Pair<Direction, Tile>> neighboringTiles = Arrays.stream(Direction.values())
                .map(d -> tileAt(t, d, grid))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
        return t.filterAttemptedConnections(neighboringTiles);
    }

    private Optional<Pair<Direction, Tile>> tileAt(Tile t, Direction d, Tile[][] tiles) {
        int row = t.getCoord().getRow() + d.getRowDiff();
        int col = t.getCoord().getCol() + d.getColDiff();
        if (0 <= row && row < tiles.length && 0 <= col && col < tiles[row].length) {
            return Optional.of(Pair.of(d, tiles[row][col]));
        }
        return Optional.empty();
    }
}
