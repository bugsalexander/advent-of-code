package aoc.util;

import java.util.List;
import java.util.Objects;

public class Tile {
    private final char c;
    private Tile(char c) {
        this.c = c;
    }

    public char getChar() {
        return c;
    }

    public static Tile of(char c) {
        return new Tile(c);
    }

    public static Tile parse(char c, List<Tile> tiles) {
        for (Tile t : tiles) {
            if (t.getChar() == c) {
                return t;
            }
        }
        throw new IllegalArgumentException("no type for char");
    }

    public static Tile[] parseLine(String line, List<Tile> tiles) {
        return line.chars()
                .mapToObj(c -> parse((char) c, tiles))
                .toArray(Tile[]::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tile tile = (Tile) o;
        return c == tile.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(c);
    }
}