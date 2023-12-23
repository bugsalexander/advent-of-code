/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day10;

import aoc.Day;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day10 implements Day {
    @Override
    public String part1(List<String> input) {
        // build the graph, find the loop, conduct BFS to find the furthest tile
        Tile[][] tiles = new Tile[input.size()][input.get(0).length()];
        Tile start = null;
        for (int row = 0; row < tiles.length; row += 1) {
            Pipe[] pipes = input.get(row).chars().mapToObj(c -> Pipe.fromChar((char) c)).toArray(Pipe[]::new);
            for (int col = 0; col < tiles[0].length; col += 1) {
                Tile t = new Tile(pipes[col], new Coord(row, col));
                if (t.getPipe() == Pipe.Start) {
                    start = t;
                }
                tiles[row][col] = t;
            }
        }

        HashSet<Tile> seen = new HashSet<>();
        List<Tile> queue = List.of(Objects.requireNonNull(start));
        int currentDistance = 0;
        while (!queue.isEmpty()) {
            List<Tile> nextQueue = new ArrayList<>();
            for (Tile tile : queue) {
                int row = tile.getCoord().getRow();
                int col = tile.getCoord().getCol();
                tile.getConnectedTiles(tileAt(row - 1, col, tiles), tileAt(row, col + 1, tiles), tileAt(row + 1, col, tiles), tileAt(row, col - 1, tiles)).stream()
                        .filter(t -> t != null && !seen.contains(t)).forEach(t -> {
                            nextQueue.add(t);
                            seen.add(t);
                        });
            }
            queue = nextQueue;
            currentDistance += 1;
        }

        return String.valueOf(currentDistance - 1);

    }

    private Tile tileAt(int row, int col, Tile[][] tiles) {
        if (0 <= row && row < tiles.length && 0 <= col && col < tiles[row].length) {
            return tiles[row][col];
        }
        return null;
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }


}
