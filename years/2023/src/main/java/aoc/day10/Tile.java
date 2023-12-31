/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day10;

import aoc.util.Posn;
import aoc.util.Pair;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Tile {
    private final TileType tileType;
    private Pipe pipe;
    private final Posn coord;
    private List<Pair<Direction, Tile>> connections;


    public Tile(TileType tileType, Posn coord) {
        this.tileType = tileType;
        this.coord = coord;
        this.pipe = this.tileType.getPipe().orElse(null);
    }

    public TileType getType() {
        return tileType;
    }

    public Optional<Pipe> getPipe() {
        return Optional.ofNullable(pipe);
    }

    public List<Direction> getPipeDirections() {
        return pipe == null ? List.of() : pipe.getDirections();
    }

    public Tile setPipe(Pipe pipe) {
        this.pipe = pipe;
        return this;
    }

    /**
     * Produces the tiles that our pipe is trying to connect to.
     * If the neighbor is returned, does not imply the neighbor is trying to connect to us.
     * @param neighbors the neighboring tiles, and what direction they are in
     * @return the tiles that our pipe is trying to connect to
     */
    public List<Pair<Direction, Tile>> filterAttemptedConnections(List<Pair<Direction, Tile>> neighbors) {
        if (tileType == TileType.Start) {
            return neighbors.stream().filter(this::isTileConnectedToUs).collect(Collectors.toList());
        }
        // we are connected to them if our connections include them
        return neighbors.stream().filter(pair -> getPipeDirections().contains(pair.getFirst())).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Tile && ((Tile) o).getCoord().equals(this.getCoord());
    }

    @Override
    public int hashCode() {
        return getCoord().hashCode();
    }

    public Posn getCoord() {
        return coord;
    }

    private boolean isTileConnectedToUs(Pair<Direction, Tile> pair) {
        if (pair.getSecond().getPipe().isEmpty()) {
            return false;
        }
        // the tile is "connected" to us if the tile is connected to the opposite the direction the tile is in (which is us)
        return pair.getSecond().getPipeDirections().contains(pair.getFirst().getOpposite());
    }

    public List<Pair<Direction, Tile>> getConnections() {
        return connections;
    }

    public Tile setConnections(List<Pair<Direction, Tile>> connections) {
        if (connections.size() != 2) {
            throw new IllegalStateException("expected connections be exactly two");
        }
        this.connections = connections;
        return this;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", pipe != null ? pipe.toString() : "", coord.toString());
    }
}
