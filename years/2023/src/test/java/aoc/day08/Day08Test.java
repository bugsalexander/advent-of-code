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

    @Test
    public void part2Test1() {
        String input = "LR\n" + "\n" + "11A = (11B, XXX)\n" + "11B = (XXX, 11Z)\n" + "11Z = (11B, XXX)\n" + "22A = (22B, XXX)\n" + "22B = (22C, 22C)\n"
                + "22C = (22Z, 22Z)\n" + "22Z = (22B, 22B)\n" + "XXX = (XXX, XXX)";
        assertEquals(day.part2(splitInput(input)), "6");
    }
}
