package aoc.day01;

import java.util.List;
import aoc.Day;

public class Day01 implements Day {

    @Override
    public String part1(List<String> input) {
        // first digit and last digit to make a 2-digit number
        // each input is a line
        int total = input.stream().mapToInt(line -> {
            List<Integer> digits = line.codePoints().filter(Character::isDigit).boxed().toList();
            int first = Character.getNumericValue(digits.get(0));
            int last = Character.getNumericValue(digits.get(digits.size() - 1));
            return (first * 10) + last;
        }).sum();
        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        return input.isEmpty() ? "" : input.get(0);
    }

}
