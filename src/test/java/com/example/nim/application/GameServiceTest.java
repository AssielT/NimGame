package com.example.nim.application;

import com.example.nim.application.exceptions.NimGameNotFoundException;
import com.example.nim.domain.*;
import com.example.nim.domain.exceptions.GameAlreadyEndedException;
import com.example.nim.domain.exceptions.InvalidAmountToRemoveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Test
    public void shouldCreateGame() throws GameAlreadyEndedException, InvalidAmountToRemoveException {
        // when
        GameState game = gameService.createGame(new CreateGameParameter(CPUDifficulty.EASY, true));

        // then
        assertThat(game.gameId()).isNotNull();
        assertThat(game.gameStatus()).isEqualTo(GameStatus.PLAYER_ONE_TURN);
        assertThat(game.matchPile()).isEqualTo(new MatchPile(13));
    }

    @Test
    public void shouldTakeMatches() throws GameAlreadyEndedException, InvalidAmountToRemoveException, NimGameNotFoundException {
        // given

        MatchPile oldAmountOfMatches = new MatchPile(13);
        NimGame nimGame = new NimGame(new GameId("1"), oldAmountOfMatches, GameStatus.PLAYER_ONE_TURN, CPUDifficulty.EASY);
        when(gameRepository.findById(nimGame.getGameId())).thenReturn(nimGame);

        // then
        GameState newGameState = gameService.takeMatches(nimGame.getGameId(), 1);

        // then
        assertThat(newGameState.matchPile()).isEqualTo(new MatchPile(12));
    }

    @Test
    public void shouldGetGameState() throws NimGameNotFoundException {
        // given

        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(13), GameStatus.PLAYER_ONE_TURN, CPUDifficulty.EASY);
        when(gameRepository.findById(nimGame.getGameId())).thenReturn(nimGame);

        // then
        GameState gameState = gameService.getGameState(nimGame.getGameId());

        // then
        assertThat(gameState.gameStatus()).isEqualTo(nimGame.getGameStatus());
        assertThat(gameState.matchPile()).isEqualTo(nimGame.getMatchPile());
        assertThat(gameState.gameId()).isEqualTo(nimGame.getGameId());
    }
}