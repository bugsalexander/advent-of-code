/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day13;

import org.testng.annotations.Test;

import aoc.DayTest;

public class Day13Test extends DayTest {
    private static final String INPUT =
            "#.##..##.\n" + "..#.##.#.\n" + "##......#\n" + "##......#\n" + "..#.##.#.\n" + "..##..##.\n" + "#.#.##.#.\n" + "\n" + "#...##." + ".#\n" + "#....#..#\n"
                    + "..##..###\n" + "#####.##.\n" + "#####.##.\n" + "..##..###\n" + "#....#..#";

    protected Day13Test() {
        super(new Day13(), INPUT);
    }

    @Test
    public void test1() {
        testPart1("405");
    }
}
