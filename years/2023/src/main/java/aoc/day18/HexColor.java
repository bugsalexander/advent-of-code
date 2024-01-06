/* **********************************************************************
 * Copyright 2024 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day18;

import aoc.util.GridDirection;

public class HexColor {
    private final GridDirection dir;
    private final int steps;
    private final String color;

    public HexColor(GridDirection dir, int steps, String color) {
        this.dir = dir;
        this.steps = steps;
        this.color = color;
    }

    public GridDirection getDir() {
        return dir;
    }

    public int getSteps() {
        return steps;
    }

    public String getColor() {
        return color;
    }
}
