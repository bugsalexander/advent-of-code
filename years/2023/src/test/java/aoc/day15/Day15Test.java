/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day15;

import org.testng.annotations.Test;

import aoc.DayTest;

public class Day15Test extends DayTest {

    private static final String INPUT = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";

    public Day15Test() {
        super(new Day15(), INPUT);
    }

    @Test
    public void test1() {
        testPart1("1320");
    }
}
