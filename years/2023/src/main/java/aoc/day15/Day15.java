/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day15;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import aoc.Day;

public class Day15 implements Day {
    private static final int BOXES = 256;

    @Override
    public String part1(List<String> input) {
        List<String> parts = List.of(input.get(0).split(","));
        BigInteger total = parts.stream()
                .map(this::computeHashAlgorithm)
                .map(BigInteger::valueOf)
                .reduce(BigInteger.ZERO, BigInteger::add);
        return total.toString();
    }

    @Override
    public String part2(List<String> input) {
        List<String> parts = List.of(input.get(0).split(","));
        /*
        256 boxes
        boxes contain lens slots
        library containing focal lenses length 1-9

        ca=1 the label is "ca"
        hash the first bit, that's the box #
        -
        remove the lens with the specified label from the box, if present
        move all the other lenses forward

        =n
        add the lens with the focal length n to the box
        mark the lens with the label maker

        - if there is already a lens in the box with the same label, update the focal length
        - otherwise, add it to the end of the lenses in the box


        AFTEr all this is arranged, total the focusing power
        (1+ box) * slot# (starts at 1) * focal length
         */
        List<List<Pair<String, Integer>>> boxes = new ArrayList<>(BOXES);
        for (int i = 0; i < BOXES; i += 1) {
            boxes.add(new ArrayList<>());
        }

        // run all instructions
        for (String sequence : parts) {
            if (sequence.charAt(sequence.length() - 1) == '-') {
                String label = sequence.substring(0, sequence.length() - 1);
                removeLens(boxes, label);
            } else {
                String label = sequence.substring(0, sequence.length() - 2);
                int focalLength = Character.getNumericValue(sequence.charAt(sequence.length() - 1));
                addOrInsertLens(boxes, label, focalLength);
            }
        }

        int maxBoxSize = boxes.stream().mapToInt(List::size).max().orElseThrow();
        System.out.printf("the max box size was %d\n", maxBoxSize);

        BigInteger total = this.calculateFocusingPower(boxes);
        return total.toString();
    }

    private int computeHashAlgorithm(String sequence) {
        /*
        start with 0
        for each char:
        - add char to total
        - total *= 17
        - total = total % 256
         */
        return sequence.chars().reduce(0, (currentValue, c) -> ((currentValue + c) * 17) % 256);
    }

    private void removeLens(List<List<Pair<String, Integer>>> boxes, String label) {
        int boxIndex = computeHashAlgorithm(label);
        List<Pair<String, Integer>> box = boxes.get(boxIndex);
        for (int i = 0; i < box.size(); i += 1) {
            Pair<String, Integer> lens = box.get(i);
            if (lens.getLeft().equals(label)) {
                box.remove(i);
                return;
            }
        }
    }

    private void addOrInsertLens(List<List<Pair<String, Integer>>> boxes, String label, int focalLength) {
        int boxIndex = computeHashAlgorithm(label);
        List<Pair<String, Integer>> box = boxes.get(boxIndex);
        for (int i = 0; i < box.size(); i += 1) {
            Pair<String, Integer> lens = box.get(i);
            if (lens.getLeft().equals(label)) {
                // replace this one with the real one
                box.set(i, Pair.of(label, focalLength));
                return;
            }
        }
        // if not already there, add
        box.add(Pair.of(label, focalLength));
    }

    private BigInteger calculateFocusingPower(List<List<Pair<String, Integer>>> boxes) {
        BigInteger total = BigInteger.ZERO;
        for (int box = 0; box < boxes.size(); box += 1) {
            int onePlusTheBoxNumber = box + 1;
            List<Pair<String, Integer>> lenses = boxes.get(box);
            for (int slotIndex = 0; slotIndex < lenses.size(); slotIndex += 1) {
                int slotNumber = slotIndex + 1;
                int focalLength = lenses.get(slotIndex).getRight();
                int focusingPower = onePlusTheBoxNumber * slotNumber * focalLength;
                total = total.add(BigInteger.valueOf(focusingPower));
            }
        }
        return total;
    }
}
