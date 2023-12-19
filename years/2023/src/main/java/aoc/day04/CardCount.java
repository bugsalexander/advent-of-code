/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day04;

public class CardCount {
    private Card card;
    private int count;

    public CardCount(Card card, int count) {
        this.card = card;
        this.count = count;
    }

    public Card getCard() {
        return card;
    }

    public CardCount setCard(Card card) {
        this.card = card;
        return this;
    }

    public int getCount() {
        return count;
    }

    public CardCount setCount(int count) {
        this.count = count;
        return this;
    }
}
