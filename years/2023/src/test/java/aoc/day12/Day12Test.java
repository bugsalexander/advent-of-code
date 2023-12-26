/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day12;

import org.testng.annotations.Test;

import aoc.DayTest;

public class Day12Test extends DayTest {
    private static final String input =
            "???.### 1,1,3\n" + ".??..??...?##. 1,1,3\n" + "?#?#?#?#?#?#?#? 1,3,1,6\n" + "????.#...#... 4,1,1\n" + "????.######..#####. 1,6," + "5\n"
                    + "?###???????? 3,2,1";

    protected Day12Test() {
        super(new Day12(), input);
    }

    @Test
    public void part1Base() {
        testPart1(".", "1");
        testPart1("# 1", "1");
        testPart1("? 1", "1");
        testPart1("?", "1");

        testPart1(". 1", "0");
        testPart1("#", "0");
        testPart1("? 2", "0");
    }

    @Test
    public void part1Step() {
        testPart1(".?", "1");
        testPart1(".? 1", "1");
        testPart1(".? 2", "0");
        testPart1("..", "1");
        testPart1(".. 1", "0");
        testPart1(".#", "0");
        testPart1(".# 1", "1");
        testPart1(".# 2", "0");

        testPart1("#?", "0");
        testPart1("#? 1", "1");
        testPart1("#? 2", "1");
        testPart1("#? 3", "0");
        testPart1("#.", "0");
        testPart1("#. 1", "1");
        testPart1("#. 2", "0");
        testPart1("##", "0");
        testPart1("## 1", "0");
        testPart1("## 2", "1");
        testPart1("## 3", "0");

        testPart1("??", "1");
        testPart1("?? 1", "2");
        testPart1("?? 2", "1");
        testPart1("?? 3", "0");
        testPart1("?? 1,1", "0");
        testPart1("?? 2,1", "0");
        testPart1("?? 1,2", "0");
        testPart1("?.", "1");
        testPart1("?. 1", "1");
        testPart1("?. 2", "0");
        testPart1("?#", "0");
        testPart1("?# 1", "1");
        testPart1("?# 2", "1");
        testPart1("?# 3", "0");
    }

    @Test
    public void part1Step1() {
        testPart1("??? 1,1", "1");
    }

    @Test
    public void testPart1() {
        // each line by line of sample input
        testPart1("???.### 1,1,3", "1");
        testPart1(".??..??...?##. 1,1,3", "4");
        testPart1("?#?#?#?#?#?#?#? 1,3,1,6", "1");
        testPart1("????.#...#... 4,1,1", "1");
        testPart1("????.######..#####. 1,6,5", "4");
        testPart1("?###???????? 3,2,1", "10");
        testPart1("21");
    }

    @Test
    public void testPart1NoUnknown() {
        String input = "#.#.### 1,1,3\n" + ".#...#....###. 1,1,3\n" + ".#.###.#.###### 1,3,1,6\n" + "####.#...#... 4,1,1\n" + "#....######..#####. 1,6,5\n"
                + ".###.##....# 3,2,1";
        testPart1(input, "6");
    }

    @Test
    public void fromInput() {
        testPart1("?#??#??????????.??.? 7,2,3,1", "12");
    }

    @Test
    public void bigInputs() {
        /*
        ??????????? 1,1,1,1
        ???????????
        15
        #.#.#.#....
        #.#.#..#...
        #.#.#...#..
        #.#.#....#.
        #.#.#.....#
        #.#..#.#...
        #.#..#..#..
        #.#..#...#.
        #.#..#....#
        #.#...#.#..
        #.#...#..#.
        #.#...#...#
        #.#....#.#.
        #.#....#..#
        #.#.....#.#

        10
        #..#.#.#...
        #..#.#..#..
        #..#.#...#.
        #..#.#....#
        #..#..#.#..
        #..#..#..#.
        #..#..#...#
        #..#...#.#.
        #..#...#..#
        #..#....#.#

        6
        #...#.#.#..
        #...#.#..#.
        #...#.#...#
        #...#..#.#.
        #...#..#..#
        #...#...#.#

        3
        #....#.#.#.
        #....#.#..#
        #....#..#.#

        1
        #.....#.#.#

        15
        20
        18
        12
        5

         */
        testPart1("??? 1,1", "1");
        testPart1("???? 1,1", "3");
        testPart1("????? 1,1", "6");
        testPart1("?????? 1,1", "10");
        testPart1("??????? 1,1", "15");

        testPart1("????? 1,1,1", "1");
        testPart1("?????? 1,1,1", "4");


        testPart1("???# 3", "1");
        testPart1("??????????? 1,1,1,1", "70");
        testPart1("???????????.### 1,1,1,1,3", "70");

        // do pass?
        testPart1("??.## 1,2", "2");
        testPart1("??.### 1,3", "2");

        // don't pass
        testPart1("??.# 1,1", "2");
        testPart1("???# 1,1", "2");

        testPart1("????# 1,2", "2");
        testPart1("?????# 1,3", "2");

        testPart1("??????????????# 1,1,1,1,3", "70");
    }

    @Test
    public void testEdgeCase() {
        testPart1("??.# 1,1", "2");
    }
}
