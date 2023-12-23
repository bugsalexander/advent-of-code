/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day09;

import aoc.Day;
import aoc.DayTest;

import org.testng.annotations.Test;

public class Day09Test extends DayTest {
    public Day09Test() {
        super(new Day09());
    }

    @Test
    public void testPart1() {
        String input = "0 3 6 9 12 15\n" + "1 3 6 10 15 21\n" + "10 13 16 21 30 45";
        testPart1(input, "114");
    }
}
