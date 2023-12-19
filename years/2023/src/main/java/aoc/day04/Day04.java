/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day04;

import java.util.List;
import java.util.stream.Collectors;
import aoc.Day;

public class Day04 implements Day {

    @Override
    public String part1(List<String> input) {
        int total = input.stream().mapToInt(line -> {
            // each line has winning numbers, |, numbers i have
            String[] cardAndNumbers = line.split(": ");
            String[] numbers = cardAndNumbers[1].split(" \\| ");
            Card card = new Card(numbers[0], numbers[1]);
            int base = 1;
            for (Integer ignored : card.getWinningNumbersIHave()) {
                base *= 2;
            }

            // 1 -> 0, 2 -> 1, 4 -> 2, etc
            return base / 2;
        }).sum();
        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        List<CardCount> cards = input.stream().map(line -> {
            String[] cardAndNumbers = line.split(": ");
            String[] numbers = cardAndNumbers[1].split(" \\| ");
            Card card = new Card(numbers[0], numbers[1]);
            return new CardCount(card, 1);
        }).collect(Collectors.toList());

        for (int i = 0; i < cards.size(); i += 1) {
            CardCount c = cards.get(i);
            int winners = c.getCard().getWinningNumbersIHave().size();
            // starting at the next card, going until we either hit the end, or the # of winners
            for (int toIncrementIdx = i + 1; toIncrementIdx < Math.min(cards.size(), i + 1 + winners); toIncrementIdx += 1) {
                CardCount cardToIncrement = cards.get(toIncrementIdx);
                // increment by the number of cards we have
                cardToIncrement.setCount(cardToIncrement.getCount() + c.getCount());
            }
        }
        int total = cards.stream().mapToInt(CardCount::getCount).sum();
        return String.valueOf(total);
    }
}
