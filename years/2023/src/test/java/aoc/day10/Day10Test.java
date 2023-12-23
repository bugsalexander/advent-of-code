/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day10;

import aoc.Day;
import aoc.DayTest;

import org.testng.annotations.Test;

public class Day10Test extends DayTest {
    public Day10Test() {
        super(new Day10());
    }

    @Test
    public void testPart1() {
        String input = "-L|F7\n" + "7S-7|\n" + "L|7||\n" + "-L-J|\n" + "L|-JF";
        testPart1(input, "4");
        String input2 = "7-F7-\n" + ".FJ|7\n" + "SJLL7\n" + "|F--J\n" + "LJ.LJ";
        testPart1(input2, "8");
    }

    @Test
    public void testPart2() {
        String input = "FF7FSF7F7F7F7F7F---7\n" + "L|LJ||||||||||||F--J\n" + "FL-7LJLJ||||||LJL-77\n" + "F--JF--7||LJLJIF7FJ-\n" + "L---JF-JLJIIIIFJLJJ7\n"
                + "|F|F-JF---7IIIL7L|7|\n" + "|FFJF7L7F-JF7IIL---7\n" + "7-L-JL7||F7|L7F-7F7|\n" + "L.L7LFJ|||||FJL7||LJ\n" + "L7JLJL-JLJLJL--JLJ.L";
        testPart2(input, "10");
    }
}
