package aoc.day01;

import java.util.HashMap;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
        Pattern digitPattern = Pattern.compile("one|two|three|four|five|six|seven|eight|nine|[1-9]");

        int total = input.stream().mapToInt(line -> {
            // get the first digit occurrence (either as digit or word)
            List<String> matches = digitPattern.matcher(line).results().map(MatchResult::group).collect(Collectors.toList());
            int first = convertToNumber(matches.get(0));
            int last = convertToNumber(matches.get(matches.size() - 1));
            System.out.printf("%d %d %s\n", first, last, line);
            return (first * 10) + last;
        }).sum();
        return String.valueOf(total);
    }

    private int convertToNumber(String match) {
        if ("123456789".contains(match)) {
            return Integer.parseInt(match);
        } else {
            return DIGIT_TO_NUMERIC.get(match);
        }
    }
}
