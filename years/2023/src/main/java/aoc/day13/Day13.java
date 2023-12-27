/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day13;

import aoc.Day;
import aoc.util.Tile;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day13 implements Day {

    private static final Tile ASH = Tile.of('.');
    private static final Tile ROCKS = Tile.of('#');
    private static final List<Tile> TILES = List.of(ASH, ROCKS);

    @Override
    public String part1(List<String> input) {
        /*
        for each puzzle:
        - if vertical mirror, return the number of lines left of reflection
        - if horizontal mirror, return the number of lines above the reflection * 100
        sum the total counts for all puzzles
         */

        // puzzle sizes might be different
        List<Tile[][]> puzzles = parse(input);
        BigInteger total = puzzles.stream()
                .map(this::countReflectionLines)
                .reduce(BigInteger.ZERO, BigInteger::add);

        return total.toString();
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    private List<Tile[][]> parse(List<String> lines) {
        List<Tile[][]> puzzles = new ArrayList<>();
        List<String> currentLines = new ArrayList<>();
        for (String line : lines) {
            if (!line.isEmpty()) {
                currentLines.add(line);
            } else if (!currentLines.isEmpty()) {
                puzzles.add(parsePuzzle(currentLines));
                currentLines = new ArrayList<>();
            }
        }
        if (!currentLines.isEmpty()) {
            puzzles.add(parsePuzzle(currentLines));
        }
        return puzzles;
    }

    private Tile[][] parsePuzzle(List<String> lines) {
        return lines.stream().map(line -> Tile.parseLine(line, TILES)).toArray(Tile[][]::new);
    }

    private BigInteger countReflectionLines(Tile[][] puzzle) {
        /*
        for [0, 1, 2, 3, 4], we treat a "reflection" between 2 and 3 as existing at "2"
        therefore, iterate excluding the last index.
        to be most efficient (return at first solution), start at the middle:
        iterate in the order [1, 2, 0, 3] or similar)

        for odd [0, 1, 2, 3, 4], order could be [2, 1, 3, 0]
        start = len / 2 - 1 + (len % 2) = 5/2-1+(5%2) = 2-1+1= 2
        2, 1, 3, 0
          -1,+2,-3

        for even [0, 1, 2, 3, 4, 5], order could be [2, 1, 3, 0, 4]

            2, 1, 3, 0, 4
              -1,+2,-3,+4
        i = 0, 1, 2, 3, 4
        index = start
        index += i * (-1)^i
        where start = (length / 2) - 1 as long as the dimensions of puzzle are >= 2
         */
        int cols = puzzle[0].length;
        int rows = puzzle.length;
        if (rows < 2 || cols < 2) {
            throw new IllegalStateException("cannot use this formula on puzzles with dims < 2");
        }

        // so we can actually cheese this by just hashing the entire column/row, which (if fast) should make
        // the comparing a lot faster since we can skip comparing entire rows for equality and just check hashes
        int[] columnHashes = IntStream.range(0, cols)
                .map(col -> Arrays.stream(puzzle)
                        .map(row -> row[col])
                        .collect(Collectors.toList())
                        .hashCode())
                .toArray();
        BigInteger colReflectionAxis = findReflectionAxis(columnHashes, cols);
        if (!colReflectionAxis.equals(BigInteger.ZERO)) {
            return colReflectionAxis;
        }

        int[] rowHashes = Arrays.stream(puzzle)
                .mapToInt(tiles -> List.of(tiles).hashCode())
                .toArray();
        BigInteger rowReflectionAxis = findReflectionAxis(rowHashes, rows);
        if (!rowReflectionAxis.equals(BigInteger.ZERO)) {
            return rowReflectionAxis.multiply(BigInteger.valueOf(100));
        }

        throw new IllegalStateException("there should have been some existing reflection");
    }

    private BigInteger findReflectionAxis(int[] colHashes, int cols) {
        int leftCol = cols / 2 - 1 + (cols % 2);
        for (int rowOffset = 0; rowOffset < cols - 1; rowOffset += 1) {
            leftCol += rowOffset * (int) Math.pow(-1, rowOffset);
            /*
            [0, 1, 2, 3, 4]
            for leftCol = 1, first compare (1, 2), then (0, 3), stop. so range [0, 2)
            for leftCol = 2, first compare (2, 3), then (1, 4), stop. so range [0, 2)
            from i = 0 to i < Math.min(leftCol + 1, length - leftCol - 1)
            leftCol = 1, min(1+1, 5-1-1) = min(2, 3) = 2 works
            leftCol = 2, min(2+1, 5-2-1) = min(3, 2) = 2 works

            [0, 1, 2, 3, 4, 5]
            for leftCol = 1, compare (1, 2), (0, 3), stop. so range [0, 2)
            for leftCol = 2, compare (2, 3), (1, 4), (0, 5), stop. so range [0, 3)
            for leftCol = 3, compare (3, 4), (2, 5), stop. so range [0, 2)
            leftCol = 1, min(1+1, 6-1-1) = min(2, 4) = 2 works
            leftCol = 2, min(2+1, 6-2-1) = min(3, 3) = 3 works
            leftCol = 3, min(3+1, 6-3-1) = min(4, 2) = 2 works
             */
            int range = Math.min(leftCol + 1, cols - leftCol - 1);
            /*
            what columns are we comparing here?
            using above examples, for example 1:
            leftCol = 1 and i = 0, compare (1, 2)
            = (leftCol + i, leftCol + 1 + i)
            leftCol = 1 and i = 1, compare (0, 3)
            = (leftCol - i, leftCol + 1 + i)
             */
            int row = leftCol;
            boolean isMirrored = IntStream.range(0, range).allMatch(comparisonIndex -> {
                int leftColHash = colHashes[row - comparisonIndex];
                int rightColHash = colHashes[row + 1 + comparisonIndex];
                return leftColHash == rightColHash;
            });
            if (isMirrored) {
                // return the # of cols to the left of the mirror
                return BigInteger.valueOf(leftCol + 1);
            }
        }
        return BigInteger.ZERO;
    }
}
