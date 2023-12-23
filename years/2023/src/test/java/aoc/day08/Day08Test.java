/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day08;

import aoc.Day;
import aoc.DayTest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class Day08Test extends DayTest {
    private static final String INPUT1 = "RL\n" + "\n" + "AAA = (BBB, CCC)\n" + "BBB = (DDD, EEE)\n" + "CCC = (ZZZ, GGG)\n" + "DDD = (DDD, DDD)\n" + "EEE = (EEE, EEE)\n"
            + "GGG = (GGG, GGG)\n" + "ZZZ = (ZZZ, ZZZ)\n";

    public Day08Test() {
        super(new Day08(), INPUT1);
    }

    @Test
    public void part1Test1() {
        assertEquals(day.part1(splitInput(INPUT1)), "2");
    }

    @Test
    public void part1Test2() {
        String input = "LLR\n" + "\n" + "AAA = (BBB, BBB)\n" + "BBB = (AAA, ZZZ)\n" + "ZZZ = (ZZZ, ZZZ)";
        assertEquals(day.part1(splitInput(input)), "6");
    }
}
