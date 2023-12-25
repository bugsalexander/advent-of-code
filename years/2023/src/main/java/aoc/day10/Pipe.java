/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day10;

import java.util.List;

public enum Pipe {
    Vertical(Direction.North, Direction.South),
    Horizontal(Direction.East, Direction.West),
    BendNorthEast(Direction.North, Direction.East),
    BendNorthWest(Direction.North, Direction.West),
    BendSouthWest(Direction.South, Direction.West),
    BendSouthEast(Direction.South, Direction.East);

    private final List<Direction> directions;

    Pipe(Direction... connections) {
        this.directions = List.of(connections);
    }

    public List<Direction> getDirections() {
        return directions;
    }
}
