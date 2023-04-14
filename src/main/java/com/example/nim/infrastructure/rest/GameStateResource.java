package com.example.nim.infrastructure.rest;

public record GameStateResource(String gameId, GameStatusResource status, int matchesLeft) {
}
