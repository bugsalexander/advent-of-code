/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day08;

public class Node {
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
}
