/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day16;

import aoc.Day;
import aoc.DayTest;

import org.testng.annotations.Test;

public class Day16Test extends DayTest {
    private static final String INPUT = ".|...\\....\n" + "|.-.\\.....\n" + ".....|-...\n" + "........|.\n" + "..........\n" + ".........\\\n" + "..../.\\\\..\n"
            + ".-.-/..|..\n" + ".|....-|.\\\n" + "..//.|....";

    protected Day16Test() {
        super(new Day16(), INPUT);
    }

    @Test
    public void test1() {
        testPart1("46");
    }

    @Test
    public void testPart2() {
        testPart2("51");
    }
}
