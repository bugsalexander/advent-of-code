/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day14;

import aoc.Day;
import aoc.DayTest;

import org.testng.annotations.Test;

public class Day14Test extends DayTest {
    private static final String INPUT = "O....#....\n" + "O.OO#....#\n" + ".....##...\n" + "OO.#O....O\n" + ".O.....O#.\n" + "O.#..O.#.#\n" + "..O..#O..O\n"
            + ".......O..\n" + "#....###..\n" + "#OO..#....";

    protected Day14Test() {
        super(new Day14(), INPUT);
    }

    @Test
    public void testPart1() {
        testPart1("136");
    }

    @Test
    public void testPart2() {
        testPart2("64");
    }
}
