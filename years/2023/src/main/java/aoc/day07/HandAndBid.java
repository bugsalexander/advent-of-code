/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day07;

public class HandAndBid implements Comparable<HandAndBid> {
    private final Hand hand;
    private final int bid;

    public HandAndBid(Hand hand, int bid) {
        this.hand = hand;
        this.bid = bid;
    }

    @Override
    public int compareTo(HandAndBid o) {
        return this.hand.compareTo(o.hand);
    }

    public int getBid() {
        return bid;
    }
}
