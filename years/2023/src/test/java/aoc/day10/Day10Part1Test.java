/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day10;

import aoc.DayTest;

import org.testng.annotations.Test;

public class Day10Part1Test extends DayTest {
    public Day10Part1Test() {
        super(new Day10());
    }

    @Test
    public void input1Simple() {
        String input = ".....\n" + ".S-7.\n" + ".|.|.\n" + ".L-J.\n" + ".....";
        testPart1(input, "4");
    }

    @Test
    public void input1() {
        String input = "-L|F7\n" + "7S-7|\n" + "L|7||\n" + "-L-J|\n" + "L|-JF";
        testPart1(input, "4");
    }

    @Test
    public void input2Simple() {
        String input2 = "..F7.\n" + ".FJ|.\n" + "SJ.L7\n" + "|F--J\n" + "LJ...";
        testPart1(input2, "8");
    }

    @Test
    public void input2() {
        String input2 = "7-F7-\n" + ".FJ|7\n" + "SJLL7\n" + "|F--J\n" + "LJ.LJ";
        testPart1(input2, "8");
    }
}
