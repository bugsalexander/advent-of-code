package aoc.day01;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class Day01Test {

    @Test
    public void testPart1(){
        // Given
        List<String> input = List.of("te1s2t", "23");

        // When
        String result = new Day01().part1(input);

        // Then
        assertEquals("35", result);
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

        assertEquals("91", new Day01().part2(List.of("9twonegs")));
    }
}
