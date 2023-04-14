package com.example.nim.domain;

public interface GameRepository {
    void save(NimGame game);

    NimGame findById(GameId gameId);
}
