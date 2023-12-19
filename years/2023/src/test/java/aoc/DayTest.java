/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;

public abstract class DayTest {

    private final Day day;
    private final List<String> lines;

    protected DayTest(Day day, String input) {
        this.day = day;
        this.lines = splitInput(input);
    }

    protected void testPart1(String actual) {
        assertEquals(day.part1(lines), actual);
    }

    protected void testPart2(String actual) {
        assertEquals(day.part2(lines), actual);
    }

    protected static List<String> splitInput(String input) {
        return Arrays.stream(input.split("\n")).collect(Collectors.toList());
    }
}
