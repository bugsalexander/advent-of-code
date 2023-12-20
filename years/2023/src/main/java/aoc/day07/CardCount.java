/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day07;

public class CardCount {
    private final Card card;
    private final int count;

    public CardCount(Card card, int count) {
        this.card = card;
        this.count = count;
    }

    public Card getCard() {
        return card;
    }

    public int getCount() {
        return count;
    }
}
