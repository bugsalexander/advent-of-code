/* **********************************************************************
 * Copyright 2023 VMware, Inc.  All rights reserved. VMware Confidential
 * *********************************************************************/

package aoc.day07;

public enum Card {
    Ace('A'),
    King('K'),
    Queen('Q'),
    Jack('J'),
    Ten('T'),
    Nine('9'),
    Eight('8'),
    Seven('7'),
    Six('6'),
    Five('5'),
    Four('4'),
    Three('3'),
    Two('2');
    
    private final char character;

    private Card(char character) {
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }

    public static Card fromChar(char c) {
        for (Card card : Card.values()) {
            if (card.getCharacter() == c) {
                return card;
            }
        }
        throw new IllegalArgumentException("char did not correspond to a card");
    }

}
