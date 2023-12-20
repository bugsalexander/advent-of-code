/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day07;

import org.testng.annotations.Test;

import aoc.DayTest;

public class Day07Test extends DayTest {
    private static final String INPUT = "32T3K 765\n" + "T55J5 684\n" + "KK677 28\n" + "KTJJT 220\n" + "QQQJA 483";

    protected Day07Test() {
        super(new Day07(), INPUT);
    }

    @Test
    public void testPart1() {
        testPart1("6440");
    }

    @Test
    public void testPart2() {
        testPart2("5905");
    }
}
