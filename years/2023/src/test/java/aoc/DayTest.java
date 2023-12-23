/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;

public abstract class DayTest {

    protected final Day day;
    private final String defaultInput;

    protected DayTest(Day day) {
        this(day, null);
    }

    protected DayTest(Day day, String defaultInput) {
        this.day = day;
        this.defaultInput = defaultInput;
    }

    protected void testPart1(String actual) {
        testPart1(defaultInput, actual);
    }

    protected void testPart1(String input, String expected) {
        assertEquals(day.part1(splitInput(input)), expected);
    }

    protected void testPart2(String actual) {
        testPart2(defaultInput, actual);
    }

    protected void testPart2(String input, String expected) {
        assertEquals(day.part2(splitInput(input)), expected);
    }

    protected static List<String> splitInput(String input) {
        return Arrays.stream(input.split("\n")).collect(Collectors.toList());
    }
}
