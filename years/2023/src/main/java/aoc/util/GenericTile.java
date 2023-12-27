/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.util;

public interface GenericTile {

    char getChar();

    static <E extends Enum<E> & GenericTile> E fromChar(int c, Class<E> enumClass) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.getChar() == c) {
                return e;
            }
        }
        throw new IllegalArgumentException("no type for char");
    }

    static <E extends GenericTile> String toGridString(E[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (E[] row : grid) {
            for (E e : row) {
                sb.append(e.getChar());
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
