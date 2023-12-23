/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tile {
    private final Pipe pipe;
    private final Coord coord;
    private List<Tile> neighbors;


    public Tile(Pipe pipe, Coord coord) {
        this.pipe = pipe;
        this.coord = coord;
    }

    public List<Tile> getNeighbors() {
        return neighbors;
    }

    public Tile setNeighbors(List<Tile> neighbors) {
        this.neighbors = neighbors;
        return this;
    }

    public Pipe getPipe() {
        return pipe;
    }

    public List<Tile> getConnectedTiles(List<Tile> adjacentTiles) {
        return getConnectedTiles(adjacentTiles.get(0), adjacentTiles.get(1), adjacentTiles.get(2), adjacentTiles.get(3));
    }

    // never eat soggy waffles!
    public List<Tile> getConnectedTiles(Tile north, Tile east, Tile south, Tile west) {
        switch (this.pipe) {
        case Vertical:
            return Arrays.asList(north, south);
        case Horizontal:
            return Arrays.asList(east, west);
        case BendNorthEast:
            return Arrays.asList(north, east);
        case BendNorthWest:
            return Arrays.asList(north, west);
        case BendSouthWest:
            return Arrays.asList(south, west);
        case BendSouthEast:
            return Arrays.asList(south, east);
        case Ground:
            return List.of();
        case Start:
            // check if any of the neighbors would include us as their neighbor
            // guaranteed to not hit NPEs/infinite loops here since there should only be one start
            List<Tile> neighbors = new ArrayList<>();
            if (north != null && north.getConnectedTiles(null, null, this, null).contains(this)) {
                neighbors.add(north);
            }
            if (east != null && east.getConnectedTiles(null, null, null, this).contains(this)) {
                neighbors.add(east);
            }
            if (south != null && south.getConnectedTiles(this, null, null, null).contains(this)) {
                neighbors.add(south);
            }
            if (west != null && west.getConnectedTiles(null, this, null, null).contains(this)) {
                neighbors.add(west);
            }
            return neighbors;
        default:
            throw new IllegalStateException("shouldn't get here");
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Tile && ((Tile) o).getCoord().equals(this.getCoord());
    }

    @Override
    public int hashCode() {
        return getCoord().hashCode();
    }

    public Coord getCoord() {
        return coord;
    }
}
