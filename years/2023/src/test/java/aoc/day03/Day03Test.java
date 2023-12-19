/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day03;

import aoc.Day;
import aoc.DayTest;

import org.testng.annotations.Test;

public class Day03Test extends DayTest {

    private static final String INPUT =
            "467..114..\n" + "...*......\n" + "..35..633.\n" + "......#...\n" + "617*......\n" + ".....+.58.\n" + "..592.....\n" + "......755.\n" + "...$.*....\n"
                    + ".664.598..";

    protected Day03Test() {
        super(new Day03(), INPUT);
    }

    @Test
    public void testPart1() {
        testPart1("4361");
    }

    @Override
    public void testPart2() {
        testPart2("467835");
    }
}
