package aoc.day02;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aoc.day01.Day01;

public class Day02Test {

    @Test
    public void testPart1(){
        String input = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green\n" + "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue\n"
                + "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red\n"
                + "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red\n" + "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green";
        // Given
        List<String> lines = Arrays.stream(input.split("\n")).collect(Collectors.toList());

        // When
        String result = new Day02().part1(lines);

        // Then
        assertEquals("8", result);
    }

    @Test
    public void testPart2(){
        // Given
        String sample = "two1nine\n" + "eightwothree\n" + "abcone2threexyz\n" + "xtwone3four\n" + "4nineeightseven2\n" + "zoneight234\n" + "7pqrstsixteen";
        List<String> input = Arrays.stream(sample.split("\n")).collect(Collectors.toList());

        // When
        String result = new Day01().part2(input);

        // Then
        assertEquals("281", result);
    }
}
