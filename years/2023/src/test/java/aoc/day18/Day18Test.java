/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day18;

import org.testng.annotations.Test;

import aoc.DayTest;

public class Day18Test extends DayTest {
    private static final String INPUT = "R 6 (#70c710)\n" + "D 5 (#0dc571)\n" + "L 2 (#5713f0)\n" + "D 2 (#d2c081)\n" + "R 2 (#59c680)\n" + "D 2 (#411b91)\n"
            + "L 5 (#8ceee2)\n" + "U 2 (#caa173)\n" + "L 1 (#1b58a2)\n" + "U 2 (#caa171)\n" + "R 2 (#7807d2)\n" + "U 3 (#a77fa3)\n" + "L 2 (#015232)\n" + "U 2 (#7a21e3)";

    public Day18Test() {
        super(new Day18(), INPUT);
    }

    @Test
    public void testPart1() {
        testPart1("62");
    }
}
