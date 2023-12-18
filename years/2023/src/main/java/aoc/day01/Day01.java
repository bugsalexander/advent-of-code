package aoc.day01;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import aoc.Day;

public class Day01 implements Day {

    private static final HashMap<String, Integer> DIGIT_TO_NUMERIC = new HashMap<>();
    static {
        DIGIT_TO_NUMERIC.put("one", 1);
        DIGIT_TO_NUMERIC.put("two", 2);
        DIGIT_TO_NUMERIC.put("three", 3);
        DIGIT_TO_NUMERIC.put("four", 4);
        DIGIT_TO_NUMERIC.put("five", 5);
        DIGIT_TO_NUMERIC.put("six", 6);
        DIGIT_TO_NUMERIC.put("seven", 7);
        DIGIT_TO_NUMERIC.put("eight", 8);
        DIGIT_TO_NUMERIC.put("nine", 9);
    }

    @Override
    public String part1(List<String> input) {
        // first digit and last digit to make a 2-digit number
        // each input is a line
        int total = input.stream().mapToInt(line -> {
            List<Integer> digits = line.codePoints().filter(Character::isDigit).boxed().collect(Collectors.toList());
            int first = Character.getNumericValue(digits.get(0));
            int last = Character.getNumericValue(digits.get(digits.size() - 1));
            return (first * 10) + last;
        }).sum();
        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        int total = input.stream().mapToInt(line -> {
            // get the first digit occurrence (either as digit or word)
            int first = convertToNumber(findFirstMatch(line));
            int last = convertToNumber(findLastMatch(line));
            return (first * 10) + last;
        }).sum();
        return String.valueOf(total);
    }

    private int convertToNumber(String match) {
        if ("1234567890".contains(match)) {
            return Integer.parseInt(match);
        } else {
            return DIGIT_TO_NUMERIC.get(match);
        }
    }

    private String findFirstMatch(String line) {
        return IntStream.range(0, line.length()).mapToObj(line::substring).flatMap(this::mapToDigitString)
                .findFirst().orElseThrow();
    }

    private String findLastMatch(String line) {
        return IntStream.range(0, line.length()).boxed().sorted((a, b) -> b - a)
                .map(line::substring).flatMap(this::mapToDigitString).findFirst().orElseThrow();
    }

    private Stream<String> mapToDigitString(String str) {
        for (Map.Entry<String, Integer> entry : DIGIT_TO_NUMERIC.entrySet()) {
            if (str.startsWith(entry.getKey())) {
                return Stream.of(entry.getKey());
            } else if (str.startsWith(String.valueOf(entry.getValue()))) {
                return Stream.of(String.valueOf(entry.getValue()));
            }
        }
        return Stream.empty();
    }
}
