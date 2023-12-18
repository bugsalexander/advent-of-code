/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public abstract class DayTest {

    private final Day day;
    private final List<String> lines;

    protected DayTest(Day day, String input) {
        this.day = day;
        this.lines = splitInput(input);
    }

    @Test
    public abstract void testPart1();

    @Test
    public abstract void testPart2();

    protected void testPart1(String actual) {
        assertEquals(day.part1(lines), actual);
    }

    protected static List<String> splitInput(String input) {
        return Arrays.stream(input.split("\n")).collect(Collectors.toList());
    }
}
