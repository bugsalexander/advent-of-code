/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day11;

import aoc.DayTest;

import org.testng.annotations.Test;

public class Day11Part1Test extends DayTest {

    public Day11Part1Test() {
        super(new Day11());
    }

    @Test
    public void test1() {
        String input = "...#......\n" + ".......#..\n" + "#.........\n" + "..........\n" + "......#...\n" + ".#........\n" + ".........#\n" + "..........\n"
                + ".......#..\n" + "#...#.....";
        testPart1(input, "374");
    }
}
