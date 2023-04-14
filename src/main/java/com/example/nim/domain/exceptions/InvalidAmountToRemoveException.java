package com.example.nim.domain.exceptions;

public class InvalidAmountToRemoveException extends Exception {
    public InvalidAmountToRemoveException(int amount) {
        super("Cannot remove amount of '" + amount + "'");
    }
}
