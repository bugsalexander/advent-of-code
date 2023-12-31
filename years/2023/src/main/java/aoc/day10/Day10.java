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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import aoc.Day;
import aoc.util.Posn;
import aoc.util.Pair;

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
        columnToRowTilesInLoop.values().forEach(list -> list.sort(Comparator.comparingInt(a -> a.getCoord().getCol())));

        // for each tile in loop, compute a direction representing which way is inwards
        // start at the top left, where the inward direction is to the right and down
        Tile prevTile = columnToRowTilesInLoop.entrySet().stream()
                .min(Comparator.comparingInt(Map.Entry::getKey)).orElseThrow()
                .getValue().get(0);
        Pair<Direction, Tile> currTile = prevTile.getConnections().get(0);
        // represent a radial direction as a pair of integers (row, col) representing a vector
        HashMap<Tile, Pair<Integer, Integer>> tileToInwardVectors = new HashMap<>();
        tileToInwardVectors.put(prevTile, Pair.of(1, 1));
        if (prevTile.getPipe().orElseThrow() != Pipe.BendSouthEast) {
            throw new IllegalStateException("the top row leftmost pipe should always be south east");
        }

        // maybe we can just do this with vectors!
        while (currTile != null) {
            // the orthodox interior-facing vector of the next tile is the one that has the higher dot product with the current interior direction
            Pair<Integer, Integer> interiorPointingVector = tileToInwardVectors.get(prevTile);
            Direction offset = currTile.getFirst();
            Pair<Integer, Integer> similarVector = getPipeOrthodoxVectors(currTile.getSecond().getPipe().orElseThrow()).stream()
                    // adjust the vectors by the position diff to make the dot product more accurate
                    .map(vector -> Pair.of(vector, dotProduct(unitVectorAdd(vector, offset), interiorPointingVector)))
                    .max(Comparator.comparingDouble(Pair::getSecond))
                    .map(Pair::getFirst)
                    .orElseThrow();
            tileToInwardVectors.put(currTile.getSecond(), similarVector);
            /*
            inward vector = (-1, 1)
            diff adjust = (1, 0)
            opposite vectors = (-1, -1), (1, 1)
            adjusted vectors = (0, -1), (2, 1)
             */

            prevTile = currTile.getSecond();
            currTile = currTile.getSecond().getConnections().stream()
                    .filter(p -> !tileToInwardVectors.containsKey(p.getSecond())).findFirst().orElse(null);
        }

        // now we conduct floodfill. add neighbors
        HashSet<Posn> seen = new HashSet<>();
        Stack<Tile> stack = new Stack<>();
        seen.add(start.getCoord());
        stack.push(start);
        while (!stack.isEmpty()) {
            Tile tile = stack.pop();
            List<Pair<Direction, Tile>> neighbors = getNeighbors(tile, tiles);
            if (tileToInwardVectors.containsKey(tile)) {
                // if this is an edge, filter out the neighbors that are not in the inward direction
                Pair<Integer, Integer> inwardVector = tileToInwardVectors.get(tile);
                // they are in the direction of the inward vector if the dot product is >= 0 ("sameness" is positive)
                List<Pair<Direction, Tile>> similarNeighbors = neighbors.stream()
                        .filter(p -> !seen.contains(p.getSecond().getCoord())
                                // either is an inward tile, or is part of the loop
                                && (dotProduct(p.getFirst().asVector(), inwardVector) >= 0 || tileToInwardVectors.containsKey(p.getSecond())))
                        .collect(Collectors.toList());
                similarNeighbors.forEach(p -> {
                    seen.add(p.getSecond().getCoord());
                    stack.push(p.getSecond());
                });
            } else {
                // if is a contained tile, just add everything not yet seen
                neighbors.stream().filter(p -> !seen.contains(p.getSecond().getCoord())).forEach(p -> {
                    seen.add(p.getSecond().getCoord());
                    stack.push(p.getSecond());
                });
            }
        }

        for (int row = 0; row < tiles.length; row += 1) {
            for (int col = 0; col < tiles[0].length; col += 1) {
                if (seen.contains(Posn.of(row, col))) {
                    if (tileToInwardVectors.containsKey(tiles[row][col])) {
//                        System.out.printf("%s ", tiles[row][col].getType().getChar());
                        System.out.printf("%s ", stringifyVector(tileToInwardVectors.get(tiles[row][col])));
                    } else {
                        System.out.print(". ");
                    }
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }

        return String.valueOf(seen.size() - tileToInwardVectors.size());
    }

    private Tile buildGraph(Tile[][] tiles, List<String> input) {
        Tile start = null;
        for (int row = 0; row < tiles.length; row += 1) {
            TileType[] tileTypes = input.get(row).chars().mapToObj(c -> TileType.fromChar((char) c)).toArray(TileType[]::new);
            for (int col = 0; col < tiles[0].length; col += 1) {
                Tile t = new Tile(tileTypes[col], Posn.of(row, col));
                if (t.getType() == TileType.Start) {
                    start = t;
                }
                tiles[row][col] = t;
            }
        }
        // set the corresponding pipe connection for start tile
        Set<Direction> ourConnections = getAttemptedConnections(Objects.requireNonNull(start), tiles)
                .stream().map(Pair::getFirst).collect(Collectors.toSet());
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
                List<Pair<Direction, Tile>> connections = getAttemptedConnections(tile, grid);
                connections.stream()
                        .map(Pair::getSecond)
                        .filter(t -> t != null && !seen.contains(t)).forEach(t -> {
                            nextQueue.add(t);
                            seen.add(t);
                            tileAction.accept(t);
                        });
                tile.setConnections(connections);
            }
            queue = nextQueue;
            currentDistance += 1;
        }
        return currentDistance;
    }

    private List<Pair<Direction, Tile>> getAttemptedConnections(Tile t, Tile[][] grid) {
        return t.filterAttemptedConnections(getNeighbors(t, grid));
    }

    private List<Pair<Direction, Tile>> getNeighbors(Tile t, Tile[][] grid) {
        return Arrays.stream(Direction.values())
                .map(d -> tileAt(t, d, grid))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<Pair<Direction, Tile>> tileAt(Tile t, Direction d, Tile[][] tiles) {
        int row = t.getCoord().getRow() + d.getRowDiff();
        int col = t.getCoord().getCol() + d.getColDiff();
        if (0 <= row && row < tiles.length && 0 <= col && col < tiles[row].length) {
            return Optional.of(Pair.of(d, tiles[row][col]));
        }
        return Optional.empty();
    }

    private List<Pair<Integer, Integer>> getPipeOrthodoxVectors(Pipe pipe) {
        // all pipes have two directions
        if (pipe == Pipe.Vertical) {
            return List.of(Pair.of(0, -1), Pair.of(0, 1));
        } else if (pipe == Pipe.Horizontal) {
            return List.of(Pair.of(1, 0), Pair.of(-1, 0));
        } else {
            // add the vectors
            List<Direction> dirs = pipe.getDirections();
            int row = dirs.get(0).getRowDiff() + dirs.get(1).getRowDiff();
            int col = dirs.get(0).getColDiff() + dirs.get(1).getColDiff();
            return List.of(Pair.of(row, col), Pair.of(-row, -col));
        }
    }

    private double dotProduct(Pair<Double, Double> v1, Pair<Integer, Integer> v2) {
        return v1.getFirst() * v2.getFirst() + v1.getSecond() * v2.getSecond();
    }

    private String stringifyVector(Pair<Integer, Integer> v) {
        if (v.getFirst() == 0 && v.getSecond() == 0) {
            return "X";
        }
        if (v.getFirst() == 0) {
            if (v.getSecond() < 0) {
                return "<";
            } else {
                return ">";
            }
        } else if (v.getSecond() == 0) {
            if (v.getFirst() < 0) {
                return "A";
            } else {
                return "V";
            }
        } else if (v.getFirst() < 0) {
            if (v.getSecond() < 0) {
                return "F";
            } else {
                return "7";
            }
        } else if (v.getFirst() > 0) {
            if (v.getSecond() < 0) {
                return "L";
            } else {
                return "J";
            }
        }
        throw new IllegalStateException("shouldn't get here");
    }

    private Pair<Double, Double> unitVectorAdd(Pair<Integer, Integer> vector, Direction offset) {
        int row = vector.getFirst() - offset.getRowDiff();
        int col = vector.getSecond() - offset.getColDiff();
        double total = Math.sqrt(row * row + col * col);
        return Pair.of(row / total, col / total);
    }
}
