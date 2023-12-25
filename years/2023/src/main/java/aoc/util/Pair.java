/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.util;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Pair<K, V> {
    private final K key;
    private final V value;
    private Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <T, U> Pair<T, U> of(T key, U value) {
        return new Pair<>(key, value);
    }

    public static <T, U> Pair<T, U> fromEntry(Map.Entry<T, U> entry) {
        return Pair.of(entry.getKey(), entry.getValue());
    }

    public static <T, U> Collector<Pair<T, U>, ?, Map<T, U>> toMap() {
        return Collectors.toMap(Pair::getKey, Pair::getValue);
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
