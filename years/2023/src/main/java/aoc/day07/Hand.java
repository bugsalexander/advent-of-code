/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day07;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Hand implements Comparable<Hand> {
    private final Card[] cards;
    private final HandType handType;

    public Hand(Card[] cards) {
        this.cards = cards;
        HashMap<Card, Integer> counts = new HashMap<>();
        for (Card c : cards) {
            counts.compute(c, (_c, count) -> count != null ? count + 1 : 1);
        }
        // sorted in ascending order
        List<CardCount> cardsOrderedByCount = counts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .map(e -> new CardCount(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        // find the matching hand type
        this.handType = Arrays.stream(HandType.values()).filter(ht ->
                ht.getNumSame().length == cardsOrderedByCount.size()
                && IntStream.range(0, cardsOrderedByCount.size())
                        .allMatch(i -> ht.getNumSame()[i] == cardsOrderedByCount.get(i).getCount()))
                .findFirst().orElseThrow();
    }

    public Card[] getCards() {
        return cards;
    }

    @Override
    public int compareTo(Hand that) {
        if (this.handType != that.handType) {
            return this.handType.compareTo(that.handType);
        }

        // otherwise, compare each card
        for (int i = 0; i < 5; i += 1) {
            if (this.cards[i] != that.cards[i]) {
                return this.cards[i].compareTo(that.cards[i]);
            }
        }

        return 0;
    }

    private enum HandType {
        FiveOfAKind(5),
        FourOfAKind(4, 1),
        FullHouse(3, 2),
        ThreeOfAKind(3, 1, 1),
        TwoPair(2, 2, 1),
        OnePair(2, 1, 1, 1),
        HighCard(1, 1, 1, 1, 1);

        private final int[] numSame;
        private HandType(int ... numSame) {
            this.numSame = numSame;
        }

        public int[] getNumSame() {
            return numSame;
        }
    }
}
