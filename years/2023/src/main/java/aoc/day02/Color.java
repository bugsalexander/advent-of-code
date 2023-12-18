/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day02;

public enum Color {
    Red("red", 0, 12), Green("green", 1, 13), Blue("blue", 2, 14);

    private final String name;
    private final int index;
    private final int maxCount;
    Color(String name, int index, int maxCount) {
        this.name = name;
        this.index = index;
        this.maxCount = maxCount;
    }

    public static Color fromString(String value) {
        for (Color color : Color.values()) {
            if (color.getName().equals(value)) {
                return color;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
    public int getIndex() {
        return index;
    }

    public int getMaxCount() {
        return maxCount;
    }
}