/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day05;

public enum SeedRequirementType {
    Seed,
    Soil,
    Fertilizer,
    Water,
    Light,
    Temperature,
    Humidity,
    Location;

    public static SeedRequirementType fromString(String s) {
        for (SeedRequirementType t : SeedRequirementType.values()) {
            if (t.name().toLowerCase().equals(s)) {
                return t;
            }
        }
        throw new IllegalArgumentException("unable to parse seed requirement type from string: " + s);
    }
}
