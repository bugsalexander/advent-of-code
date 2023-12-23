/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day08;

import aoc.Day;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day08 implements Day {

    @Override
    public String part1(List<String> input) {
        // try the naive approach first?
        Direction[] directions = input.get(0).chars().mapToObj(c ->
                Direction.fromChar((char) c)).toArray(Direction[]::new);
        Map<String, Node> idToNodeMap = parseIdToNodeMap(input.subList(2, input.size()));

        Node current = idToNodeMap.get(Node.START);
        int directionIndex = 0;
        int steps = 0;
        while (!current.getId().equals(Node.FINISH)) {
            Direction d = directions[directionIndex];
            current = current.next(d);
            directionIndex = (directionIndex + 1) % directions.length;
            steps += 1;
        }

        return String.valueOf(steps);
    }

    @Override
    public String part2(List<String> input) {
        Direction[] directions = input.get(0).chars().mapToObj(c ->
                Direction.fromChar((char) c)).toArray(Direction[]::new);
        Map<String, Node> idToNodeMap = parseIdToNodeMap(input.subList(2, input.size()));

        List<Node> nodes = idToNodeMap.values().stream().filter(Node::isStart).collect(Collectors.toList());
        // for each node, find all the possible finishes, and at what cycles they will repeat
        int distanceToCheck = (idToNodeMap.size() * directions.length);
        Map<Node, int[]> nodeToFinishCycles = nodes.stream()
                .collect(Collectors.toMap(Function.identity(), node -> node.getFinishCycles(directions, distanceToCheck)));

        // find the least common multiplier between all the different combinations
        // for some reason, each one has a single number?
        long[] cycles = nodeToFinishCycles.values().stream()
                .peek(steps -> {
                    if (steps.length != 1) {
                        throw new IllegalStateException("had more than one cycle to a finish node");
                    }
                })
                .mapToLong(steps -> steps[0])
                .toArray();

        return String.valueOf(lcm(cycles));
    }

    private Map<String, Node> parseIdToNodeMap(List<String> nodeStrings) {
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
        return idToNodeMap;
    }

    private String[] parseSingleNode(String nodeString) {
        String[] parts = nodeString.split(" = ");
        String nodeId = parts[0];
        String withoutParens = parts[1].substring(1, parts[1].length() - 1);
        String[] leftRightParts = withoutParens.split(", ");
        return new String[] { nodeId, leftRightParts[0], leftRightParts[1] };
    }

    // following functions taken from: https://stackoverflow.com/questions/4201860/how-to-find-gcd-lcm-on-a-set-of-numbers

    private static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }
    
    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    private static long lcm(long[] input) {
        long result = input[0];
        for(int i = 1; i < input.length; i++) {
            result = lcm(result, input[i]);
        }
        return result;
    }
}
