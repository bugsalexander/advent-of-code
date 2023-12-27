/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day13;

import aoc.Day;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
                .map(puzzle -> this.countReflectionLines(puzzle, Set.of(), Set.of()))
                .map(BigInteger::valueOf)
                .reduce(BigInteger.ZERO, BigInteger::add);

        return total.toString();
    }

    @Override
    public String part2(List<String> input) {
        List<Tile[][]> puzzles = parse(input);
        BigInteger total = puzzles.stream()
                .map(this::permuteSmudgeAndCountReflectionLines)
                .map(BigInteger::valueOf)
                .reduce(BigInteger.ZERO, BigInteger::add);

        return total.toString();
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

    private int countReflectionLines(Tile[][] puzzle, Set<Integer> verticalToSkip, Set<Integer> horizontalToSkip) {
        int vertical = findVerticalReflection(puzzle, verticalToSkip);
        if (vertical != -1) {
            return vertical + 1;
        }
        return (findHorizontalReflection(puzzle, horizontalToSkip) + 1) * 100;
    }

    private int findVerticalReflection(Tile[][] puzzle, Set<Integer> leftColsToSkip) {
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
        if (cols < 2) {
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
        return findReflectionAxis(columnHashes, cols, leftColsToSkip);
    }

    private int findHorizontalReflection(Tile[][] puzzle, Set<Integer> topRowsToSkip) {
        int rows = puzzle.length;
        if (rows < 2) {
            throw new IllegalStateException("cannot use this formula on puzzles with dims < 2");
        }
        int[] rowHashes = Arrays.stream(puzzle)
                .mapToInt(tiles -> List.of(tiles).hashCode())
                .toArray();
        return findReflectionAxis(rowHashes, rows, topRowsToSkip);
    }

    private int findReflectionAxis(int[] colHashes, int cols, Set<Integer> leftColsToSkip) {
        int leftCol = cols / 2 - 1 + (cols % 2);
        for (int rowOffset = 0; rowOffset < cols - 1; rowOffset += 1) {
            leftCol += rowOffset * (int) Math.pow(-1, rowOffset);
            if (leftColsToSkip.contains(leftCol)) {
                continue;
            }
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
                return leftCol;
            }
        }
        return -1;
    }

    private int permuteSmudgeAndCountReflectionLines(Tile[][] puzzle) {
        // the old reflection might still be valid, so skip it if that's the old one
        Set<Integer> verticalToSkip = Set.of(findVerticalReflection(puzzle, Set.of()));
        Set<Integer> horizontalToSkip = Set.of();
        if (verticalToSkip.contains(-1)) {
            verticalToSkip = Set.of();
            horizontalToSkip = Set.of(findHorizontalReflection(puzzle, Set.of()));
        }

        for (int row = 0; row < puzzle.length; row += 1) {
            for (int col = 0; col < puzzle[0].length; col += 1) {
                // try flipping this bad boi
                Tile original = puzzle[row][col];
                puzzle[row][col] = original == ASH ? ROCKS : ASH;
                int solution = countReflectionLines(puzzle, verticalToSkip, horizontalToSkip);
                if (solution != 0) {
                    return solution;
                }
                puzzle[row][col] = original;
            }
        }

        throw new IllegalStateException("no possible flip was found");
    }
}
