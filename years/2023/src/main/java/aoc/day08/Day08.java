/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day08;

import aoc.Day;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day08 implements Day {
    private static final String START = "AAA";
    private static final String FINISH = "ZZZ";

    @Override
    public String part1(List<String> input) {
        // try the naive approach first?
        String directionString = input.get(0);
        List<String> nodeStrings = input.subList(2, input.size());

        Direction[] directions = directionString.chars().mapToObj(c ->
                Direction.fromChar((char) c)).toArray(Direction[]::new);
        String[][] parsedNodeStrings = nodeStrings.stream()
                .map(this::parseSingleNode)
                .toArray(String[][]::new);
        Map<String, Node> idToNodeMap = Arrays.stream(parsedNodeStrings)
                .map(nodeString -> new Node(nodeString[0]))
                .collect(Collectors.toMap(Node::getId, Function.identity()));
        for (String[] nodeParts : parsedNodeStrings) {
            Node node = idToNodeMap.get(nodeParts[0]);
            Node left = idToNodeMap.get(nodeParts[1]);
            Node right = idToNodeMap.get(nodeParts[2]);
            node.setLeft(left).setRight(right);
        }

        Node current = idToNodeMap.get(START);
        int directionIndex = 0;
        int steps = 0;
        while (!current.getId().equals(FINISH)) {
            Direction d = directions[directionIndex];
            current = current.next(d);
            directionIndex = (directionIndex + 1) % directions.length;
            steps += 1;
        }

        return String.valueOf(steps);
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    private String[] parseSingleNode(String nodeString) {
        String[] parts = nodeString.split(" = ");
        String nodeId = parts[0];
        String withoutParens = parts[1].substring(1, parts[1].length() - 1);
        String[] leftRightParts = withoutParens.split(", ");
        return new String[] { nodeId, leftRightParts[0], leftRightParts[1] };
    }
}
