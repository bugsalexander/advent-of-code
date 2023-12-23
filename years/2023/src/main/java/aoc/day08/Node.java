/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node {
    public static final String START = "AAA";
    public static final String FINISH = "ZZZ";
    private static final char START_CHAR = START.charAt(0);
    private static final char FINISH_CHAR = FINISH.charAt(0);

    private final String id;
    private Node left;
    private Node right;

    public Node(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Node getLeft() {
        return left;
    }

    public Node setLeft(Node left) {
        this.left = left;
        return this;
    }

    public Node getRight() {
        return right;
    }

    public Node setRight(Node right) {
        this.right = right;
        return this;
    }

    public Node next(Direction d) {
        if (d == Direction.Left) {
            return left;
        } else {
            return right;
        }
    }

    public boolean isStart() {
        return getLastCharOfId() == START_CHAR;
    }

    public boolean isFinish() {
        return getLastCharOfId() == FINISH_CHAR;
    }

    public int[] getFinishCycles(Direction[] directions, int distance) {
        HashMap<String, List<Integer>> foundFinishes = new HashMap<>();
        Node current = this;
        for (int i = 0; i < distance; i += 1) {
            if (current.isFinish()) {
                List<Integer> finishes = foundFinishes.computeIfAbsent(current.getId(), k -> new ArrayList<>());
                finishes.add(i);
            }
            current = current.next(directions[i % directions.length]);
        }

        // we should have run into finish
        if (foundFinishes.size() == 0) {
            throw new IllegalStateException("didn't find any finish nodes");
        }
        // every single found finish should have been run into at least twice
        if (foundFinishes.values().stream().anyMatch(d -> d.size() < 2)) {
            throw new IllegalStateException("only ran into some finish nodes once");
        }

        // calculate the common cycles of each finish
        return foundFinishes.values().stream().mapToInt(distances -> {
            List<Integer> diffs = new ArrayList<>();
            for (int d : distances) {
                if (diffs.stream().noneMatch(diff -> d % diff == 0)) {
                    diffs.add(d);
                }
            }
            if (diffs.size() != 1) {
                // i would expect this to happen, but i guess it doesn't... strange
                throw new IllegalStateException("cycle had more than one diff");
            }
            return diffs.get(0);
        }).toArray();
    }

    private char getLastCharOfId() {
        return id.charAt(id.length() - 1);
    }
}
