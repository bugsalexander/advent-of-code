package aoc.day02;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class Day02Test {

    String input = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green\n" + "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue\n"
            + "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red\n"
            + "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red\n" + "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green";
    List<String> lines = Arrays.stream(input.split("\n")).collect(Collectors.toList());

    @Test
    public void testPart1(){
        // When
        String result = new Day02().part1(lines);

        // Then
        assertEquals(result, "8");
    }

    @Test
    public void testPart2(){
        // When
        String result = new Day02().part2(lines);

        // Then
        assertEquals(result, "2286");
    }
}
