/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day12;

import aoc.Day;
import aoc.DayTest;

import org.testng.annotations.Test;

public class Day12Test extends DayTest {
    private static final String input = "???.### 1,1,3\n" + ".??..??...?##. 1,1,3\n" + "?#?#?#?#?#?#?#? 1,3,1,6\n" + "????.#...#... 4,1,1\n" + "????.######..#####. 1,6,"
            + "5\n"
            + "?###???????? 3,2,1";
    protected Day12Test() {
        super(new Day12(), input);
    }

    @Test
    public void testPart1() {
        testPart1("21");
    }
}
