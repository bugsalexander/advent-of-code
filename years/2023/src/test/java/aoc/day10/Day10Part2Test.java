/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day10;

import aoc.DayTest;

import org.testng.annotations.Test;

public class Day10Part2Test extends DayTest {
    public Day10Part2Test() {
        super(new Day10());
    }

    @Test
    public void testPart2() {
        String input1 = "...........\n" + ".S-------7.\n" + ".|F-----7|.\n" + ".||.....||.\n" + ".||.....||.\n" + ".|L-7.F-J|.\n" + ".|..|.|..|.\n" + ".L--J.L--J.\n"
                + "...........";
        testPart2(input1, "4");
        String input = "FF7FSF7F7F7F7F7F---7\n" + "L|LJ||||||||||||F--J\n" + "FL-7LJLJ||||||LJL-77\n" + "F--JF--7||LJLJ7F7FJ-\n" + "L---JF-JLJ.||-FJLJJ7\n"
                + "|F|F-JF---7F7-L7L|7|\n" + "|FFJF7L7F-JF7|JL---7\n" + "7-L-JL7||F7|L7F-7F7|\n" + "L.L7LFJ|||||FJL7||LJ\n" + "L7JLJL-JLJLJL--JLJ.L";
        testPart2(input, "10");
    }
}
