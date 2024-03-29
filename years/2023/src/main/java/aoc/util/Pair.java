/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.util;

/**
 * @deprecated instead of using this, use {@link org.apache.commons.lang3.tuple.Pair}
 */
public class Pair<F, S> {
    private final F first;
    private final S second;
    private Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F, S> Pair<F, S> of(F key, S value) {
        return new Pair<>(key, value);
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", first.toString(), second.toString());
    }
}
