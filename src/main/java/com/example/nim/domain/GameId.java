package com.example.nim.domain;


import java.util.Objects;

public record GameId(String value) {
    public GameId {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("GameId must not be null");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
