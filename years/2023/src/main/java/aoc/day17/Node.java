/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day17;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class Node {
    private final int cost;
    private List<Pair<Integer, Node>> neighbors = List.of();

    public Node(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public List<Pair<Integer, Node>> getNeighbors() {
        return neighbors;
    }

    public Node setNeighbors(List<Pair<Integer, Node>> neighbors) {
        this.neighbors = neighbors;
        return this;
    }
}
