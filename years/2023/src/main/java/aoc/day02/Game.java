/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day02;

import java.util.Objects;

public class Game {
    private int id;
    private int[][] pulls;

    public int getId() {
        return id;
    }

    public Game setId(int id) {
        this.id = id;
        return this;
    }

    public int[][] getPulls() {
        return pulls;
    }

    public Game setPulls(int[][] pulls) {
        this.pulls = pulls;
        return this;
    }

    public Game(int id, int[][] pulls) {
        this.id = id;
        this.pulls = pulls;
    }

    public static Game fromString(String line) {
        String[] idAndRecordParts = line.split(":\\W*");
        int id = Integer.parseInt(idAndRecordParts[0].substring(5));
        // each should look like "blue 1, red 2"
        String[] pullParts = idAndRecordParts[1].split("\\W*;\\W*");

        // in the order: red | blue | green
        int[][] pulls = new int[pullParts.length][3];
        for (int i = 0; i < pullParts.length; i += 1) {
            String pull = pullParts[i];
            // each should look like "blue 2"
            String[] colorCounts = pull.split("\\W*,\\W*");
            for (String colorCount : colorCounts) {
                // should be ["blue", "2"]
                String[] colorAndCount = colorCount.split(" ");
                int count = Integer.parseInt(colorAndCount[0]);
                Color color = Color.fromString(colorAndCount[1]);
                pulls[i][Objects.requireNonNull(color).getIndex()] = count;
            }
        }

        return new Game(id, pulls);
    }

    public boolean isValid() {
        // this game is valid if every pull does not have colors which exceed their max counts
        for (int[] count : getPulls()) {
            for (Color color : Color.values()) {
                // the pull is invalid if any color has count > its max
                if (count[color.getIndex()] > color.getMaxCount()) {
                    return false;
                }
            }
        }

        return true;
    }
}
