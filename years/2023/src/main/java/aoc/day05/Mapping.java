/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day05;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a mapping of values from source to desgination
 */
public class Mapping {

    private SeedRequirementType sourceType;
    private SeedRequirementType destinationType;
    private List<SourceDestinationRange> ranges;

    public Mapping(SeedRequirementType sourceType, SeedRequirementType destinationType, List<SourceDestinationRange> ranges) {
        this.sourceType = sourceType;
        this.destinationType = destinationType;
        this.ranges = ranges;
    }

    public SeedRequirementType getSourceType() {
        return sourceType;
    }

    public Mapping setSourceType(SeedRequirementType sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public SeedRequirementType getDestinationType() {
        return destinationType;
    }

    public Mapping setDestinationType(SeedRequirementType destinationType) {
        this.destinationType = destinationType;
        return this;
    }

    public List<SourceDestinationRange> getRanges() {
        return ranges;
    }

    public Mapping setRanges(List<SourceDestinationRange> ranges) {
        this.ranges = ranges;
        return this;
    }

    public static Mapping fromString(List<String> strings) {
        // first line in the format "seed-to-soil map:"
        String[] title = strings.remove(0).split(" ");
        String[] titleParts = title[0].split("-");
        SeedRequirementType source = SeedRequirementType.fromString(titleParts[0]);
        SeedRequirementType dest = SeedRequirementType.fromString(titleParts[2]);
        List<SourceDestinationRange> ranges = strings.stream().map(s -> {
            // destStart sourceStart range (how long each mapping is)
            List<Integer> parts = Day05.parseNumbers(s);
            return new SourceDestinationRange(parts.get(0), parts.get(1), parts.get(2));
        }).collect(Collectors.toList());

        return new Mapping(source, dest, ranges);
    }

    public int mapSourceToDestination(int currentValue) {
        for (SourceDestinationRange range : this.ranges) {
            if (range.getSourceStart() <= currentValue && currentValue < range.getSourceStart() + range.getRange()) {
                // then it matches
                int offset = currentValue - range.getSourceStart();
                return range.getDestinationStart() + offset;
            }
        }

        // Any source numbers that aren't mapped correspond to the same destination number. So, seed number 10 corresponds to soil number 10.
        return currentValue;
    }
}
