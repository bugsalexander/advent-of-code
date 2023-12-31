/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.Pair;

import aoc.Day;
import aoc.util.GridDirection;
import aoc.util.Posn;

public class Day17 implements Day {

    private static final int VERTICAL_INDEX = 0;
    private static final int HORIZONTAL_INDEX = 1;

    @Override
    public String part1(List<String> input) {
        /*
        [[left], [left left], [left left left], [right], ...]
        dirs -> heat + bestsolution not including the same direction and the reverse direction
        take the min

        epiphany: as we expand in DFS, we need to keep track of "potential solutions". cannot decide
        the "best" solution until we know the best solutions of all of our neighbors
         */

        /*
        essentially, the new problem is how do we convert this to a graph, and then use dijkstra's?
        each node becomes two nodes, where the first node only points to nodes vertically,
        and the second node only points to nodes horizontally.
         */
        List<List<GridDirection>> verticalDirections = buildDirections(List.of(GridDirection.Up, GridDirection.Down), 1, 3);
        List<List<GridDirection>> horizontalDirections = buildDirections(List.of(GridDirection.Left, GridDirection.Right), 1, 3);
        return solve(input, verticalDirections, horizontalDirections);
    }

    @Override
    public String part2(List<String> input) {
        List<List<GridDirection>> verticalDirections = buildDirections(List.of(GridDirection.Up, GridDirection.Down), 4, 10);
        List<List<GridDirection>> horizontalDirections = buildDirections(List.of(GridDirection.Left, GridDirection.Right), 4, 10);
        return solve(input, verticalDirections, horizontalDirections);
    }

    private String solve(List<String> input, List<List<GridDirection>> vertical, List<List<GridDirection>> horizontal) {
        Node[][][] graph = buildGraph(input, vertical, horizontal);
        List<Node> nodes = Arrays.stream(graph).flatMap(Arrays::stream).flatMap(Arrays::stream).collect(Collectors.toList());
        Node start = new Node(0).setNeighbors(Arrays.stream(graph[0][0]).map(n -> Pair.of(0, n)).collect(Collectors.toList()));
        Node end = new Node(0);
        nodes.addAll(List.of(start, end));
        for (Node node : graph[graph.length - 1][graph[0].length - 1]) {
            List<Pair<Integer, Node>> neighbors = new ArrayList<>(node.getNeighbors());
            neighbors.add(Pair.of(0, end));
            node.setNeighbors(neighbors);
        }
        int minDistance = runDijkstra(start, end, nodes);
        return String.valueOf(minDistance);
    }

    private Node[][][] buildGraph(List<String> input, List<List<GridDirection>> vertical, List<List<GridDirection>> horizontal) {
        int[][] grid = input.stream().map(line -> line.chars().map(Character::getNumericValue).toArray()).toArray(int[][]::new);
        // node that points vertically, and horizontally
        Node[][][] nodes = new Node[grid.length][grid[0].length][2];
        for (int row = 0; row < grid.length; row += 1) {
            for (int col = 0; col < grid[0].length; col += 1) {
                nodes[row][col][0] = new Node(grid[row][col]);
                nodes[row][col][1] = new Node(grid[row][col]);
            }
        }
        for (int row = 0; row < grid.length; row += 1) {
            for (int col = 0; col < grid[0].length; col += 1) {
                Posn p = Posn.of(row, col);
                nodes[row][col][VERTICAL_INDEX].setNeighbors(getNeighbors(nodes, p, vertical, HORIZONTAL_INDEX));
                nodes[row][col][HORIZONTAL_INDEX].setNeighbors(getNeighbors(nodes, p, horizontal, VERTICAL_INDEX));
            }
        }
        return nodes;
    }

    private List<Pair<Integer, Node>> getNeighbors(Node[][][] nodes, Posn p, List<List<GridDirection>> directions, int nodeIndex) {
        return directions.stream()
                .flatMap(d -> getNeighbor(nodes, p, d).stream())
                .map(pair -> Pair.of(pair.getLeft(), pair.getRight().at(nodes)[nodeIndex]))
                .collect(Collectors.toList());
    }

    /**
     * Produces a pair of the total value of all the squares traveled into, relative to the starting posn, plus the relative posn.
     */
    private Optional<Pair<Integer, Posn>> getNeighbor(Node[][][] grid, Posn p, List<GridDirection> directions) {
        // sum the diff
        int total = 0;
        Posn curr = p;
        for (GridDirection d : directions) {
            curr = Posn.add(curr, Posn.fromPair(d.getVector()));
            if (curr.withinBounds(grid.length, grid[0].length)) {
                total += grid[curr.getRow()][curr.getCol()][0].getCost();
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(Pair.of(total, curr));
    }

    private int runDijkstra(Node start, Node end, List<Node> nodes) {
        Map<Node, Integer> tentativeDistance = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(n -> tentativeDistance.getOrDefault(n, Integer.MAX_VALUE)));
        tentativeDistance.put(start, 0);
        priorityQueue.addAll(nodes);

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.remove();
            if (!tentativeDistance.containsKey(current)) {
                // skip if we've already visited it
                // or if tentative distance does not contain it => there is no connection to start
                continue;
            }

            // for the current node, consider all of its unvisited neighbors
            for (Pair<Integer, Node> pair : current.getNeighbors()) {
                int tentativeDistanceThroughCurrentNode = tentativeDistance.get(current) + pair.getLeft();
                int currentTentativeDistance = tentativeDistance.getOrDefault(pair.getRight(), Integer.MAX_VALUE);
                if (tentativeDistanceThroughCurrentNode < currentTentativeDistance) {
                    tentativeDistance.put(pair.getRight(), tentativeDistanceThroughCurrentNode);
                    // force queue recalculation by remove and add
                    // this is sort of slow, but whatever
                    priorityQueue.remove(pair.getRight());
                    priorityQueue.add(pair.getRight());
                }
            }
        }

        return tentativeDistance.get(end);
    }

    private static List<List<GridDirection>> buildDirections(List<GridDirection> bases, int min, int max) {
        // map bases -> list<multiplied base>
        Map<GridDirection, List<List<GridDirection>>> directions = new HashMap<>();
        // for each dir, build up a list of that direction i times
        for (GridDirection base : bases) {
            for (int count = min; count <= max; count += 1) {
                List<GridDirection> multipliedBase = IntStream.range(0, count).mapToObj(d -> base).collect(Collectors.toList());
                directions.computeIfAbsent(base, _b -> new ArrayList<>()).add(multipliedBase);
            }
        }
        return directions.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}
