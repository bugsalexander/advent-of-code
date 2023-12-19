/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day04;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Card {
    List<Integer> winningNumbersIHave;
    List<Integer> winningNumbers;
    List<Integer> numbersIHave;

    public Card(String winningNumbers, String numbersIHave) {
        this.winningNumbers = parseNumbers(winningNumbers);
        this.numbersIHave = parseNumbers(numbersIHave);
        this.winningNumbersIHave = calculateWinningNumbersIHave();
    }

    public boolean isWinner() {
        return !winningNumbersIHave.isEmpty();
    }

    public List<Integer> getWinningNumbersIHave() {
        return winningNumbersIHave;
    }

    public List<Integer> getWinningNumbers() {
        return winningNumbers;
    }

    public Card setWinningNumbers(List<Integer> winningNumbers) {
        this.winningNumbers = winningNumbers;
        return this;
    }

    public List<Integer> getNumbersIHave() {
        return numbersIHave;
    }

    public Card setNumbersIHave(List<Integer> numbersIHave) {
        this.numbersIHave = numbersIHave;
        return this;
    }

    private List<Integer> parseNumbers(String s) {
        return Arrays.stream(s.split(" ")).filter(ss -> !ss.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private List<Integer> calculateWinningNumbersIHave() {
        Set<Integer> winningNumbers = new HashSet<>(getWinningNumbers());
        return numbersIHave.stream().filter(winningNumbers::contains).collect(Collectors.toList());
    }

}
