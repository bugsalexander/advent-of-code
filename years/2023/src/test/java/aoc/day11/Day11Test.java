/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day11;

import java.math.BigInteger;

import org.testng.Assert;
import org.testng.annotations.Test;

import aoc.DayTest;

import static org.testng.Assert.assertEquals;

public class Day11Test extends DayTest {

    Day11 day11 = new Day11();
    private static final String input =
            "...#......\n" + ".......#..\n" + "#.........\n" + "..........\n" + "......#...\n" + ".#........\n" + ".........#\n" + "......." + "...\n" + ".......#..\n"
                    + "#...#.....";

    public Day11Test() {
        super(new Day11());
    }

    @Test
    public void test1() {
        testPart1(input, "374");
    }

    @Test
    public void testPart2Test1() {
        assertEquals(day11.filterGalaxiesAndCountEmptySpace(splitInput(input), BigInteger.valueOf(10)), BigInteger.valueOf(1030));
    }

    @Test
    public void testPart2Test2() {
        assertEquals(day11.filterGalaxiesAndCountEmptySpace(splitInput(input), BigInteger.valueOf(100)), BigInteger.valueOf(8410));
    }
}
