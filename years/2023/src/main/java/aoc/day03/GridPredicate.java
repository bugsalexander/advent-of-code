/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day03;

public interface GridPredicate {
    public boolean test(int row, int col, char[][] grid);
}
