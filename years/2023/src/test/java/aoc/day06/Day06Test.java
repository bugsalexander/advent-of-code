/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day06;

import aoc.Day;
import aoc.DayTest;

import org.testng.annotations.Test;

public class Day06Test extends DayTest {
    private static final String INPUT = "Time:      7  15   30\n" + "Distance:  9  40  200";
    protected Day06Test() {
        super(new Day06(), INPUT);
    }

    @Test
    public void part1Test() {
        testPart1("288");
    }

    @Test
    public void part2t() {
        testPart2("71503");
    }
}
