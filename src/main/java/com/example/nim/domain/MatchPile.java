package com.example.nim.domain;

public record MatchPile(int value) {

    public MatchPile {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid amount of Matches in Pile");
        }
    }

    public MatchPile removeMatch(int amountToRemove) {
        return new MatchPile(this.value - amountToRemove);
    }

    public boolean isEmpty() {
        return this.value == 0;
    }

}
