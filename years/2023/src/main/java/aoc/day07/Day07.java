/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day07;

import aoc.Day;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day07 implements Day {
    @Override
    public String part1(List<String> input) {
        // list of hands, order them by hands
        // card types are AKQJT98765432
        List<HandAndBid> hands = parse(input, false);
        return getTotalValue(hands);
    }

    @Override
    public String part2(List<String> input) {
        List<HandAndBid> hands = parse(input, true);
        return getTotalValue(hands);
    }

    private List<HandAndBid> parse(List<String> lines, boolean useJoker) {
        return lines.stream().map(l -> {
            List<String> parts = Arrays.stream(l.split(" ")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            Hand hand = new Hand(parts.get(0).chars().mapToObj(c -> Card.fromChar((char) c, useJoker)).toArray(Card[]::new));
            int bid = Integer.parseInt(parts.get(1));
            return new HandAndBid(hand, bid);
        }).collect(Collectors.toList());
    }

    private String getTotalValue(List<HandAndBid> hands) {
        hands.sort(null);
        int total = 0;
        for (int i = 0; i < hands.size(); i += 1) {
            HandAndBid hand = hands.get(i);
            int rank = hands.size() - i;
            total += rank * hand.getBid();
        }

        return String.valueOf(total);
    }
}
