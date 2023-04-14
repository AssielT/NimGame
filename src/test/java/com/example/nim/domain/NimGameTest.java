package com.example.nim.domain;

import com.example.nim.domain.exceptions.GameAlreadyEndedException;
import com.example.nim.domain.exceptions.InvalidAmountToRemoveException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NimGameTest {

    @Test
    public void shouldRemoveMatches() throws GameAlreadyEndedException, InvalidAmountToRemoveException {
        // given
        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(13), GameStatus.PLAYER_ONE_TURN, CPUDifficulty.EASY);

        // when
        nimGame.removeMatches(2);

        // then
        assertThat(nimGame.getMatchPile()).isEqualTo(new MatchPile(11));
    }

    @Test
    public void shouldChangePlayerTurnWhenMatchesRemoved() throws GameAlreadyEndedException, InvalidAmountToRemoveException {
        // given
        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(13), GameStatus.PLAYER_ONE_TURN, CPUDifficulty.EASY);

        // when
        nimGame.removeMatches(2);

        // then
        assertThat(nimGame.getGameStatus()).isEqualTo(GameStatus.PLAYER_TWO_TURN);
    }

    @Test
    public void shouldNotAllowToRemoveMatchesWhenInvalidAmount() {
        // given
        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(13), GameStatus.PLAYER_ONE_TURN, CPUDifficulty.EASY);

        // when, then
        assertThatThrownBy(() -> nimGame.removeMatches(4)).isInstanceOf(InvalidAmountToRemoveException.class);
        assertThat(nimGame.getMatchPile()).isEqualTo(new MatchPile(13));
    }

    @Test
    public void shouldNotAllowToRemoveMatchesWhenGameEnded() {
        // given
        GameStatus gameStatusEnded = GameStatus.PLAYER_ONE_WON; // game ended when Status is either player one won or player 2 won
        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(13), gameStatusEnded, CPUDifficulty.EASY);

        // when, then
        assertThatThrownBy(() -> nimGame.removeMatches(4)).isInstanceOf(GameAlreadyEndedException.class);
        assertThat(nimGame.getMatchPile()).isEqualTo(new MatchPile(13));
    }

    @Test
    public void changeGameStatusToPlayerOneWonWhenPlayerTwoTakesLastMatch() throws GameAlreadyEndedException, InvalidAmountToRemoveException {
        // given
        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(1), GameStatus.PLAYER_TWO_TURN, CPUDifficulty.EASY);

        // when
        nimGame.removeMatches(1);

        // then
        assertThat(nimGame.getGameStatus()).isEqualTo(GameStatus.PLAYER_ONE_WON);
    }

    @Test
    public void changeGameStatusToPlayerTwoWonWhenPlayerOneTakesLastMatch() throws GameAlreadyEndedException, InvalidAmountToRemoveException {
        // given
        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(1), GameStatus.PLAYER_ONE_TURN, CPUDifficulty.EASY);

        // when
        nimGame.removeMatches(1);

        // then
        assertThat(nimGame.getGameStatus()).isEqualTo(GameStatus.PLAYER_TWO_WON);
    }

    @Test
    public void maxNumberOfPossibleRemovableMatchesIsThreeWhenMoreAreInMatchPile() {
        // given
        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(13), GameStatus.PLAYER_ONE_TURN, CPUDifficulty.EASY);

        // when
        int maxAmountOfPossibleRemovableMatches = nimGame.getMaxAmountOfPossibleRemovableMatches();

        // then
        assertThat(maxAmountOfPossibleRemovableMatches).isEqualTo(3);
    }

    @Test
    public void shouldNotRemoveMoreMatchesThanExist() {
        // given
        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(2), GameStatus.PLAYER_ONE_TURN, CPUDifficulty.EASY);

        // when, then
        assertThatThrownBy(() -> nimGame.removeMatches(3)).isInstanceOf(InvalidAmountToRemoveException.class);
        assertThat(nimGame.getMatchPile()).isEqualTo(new MatchPile(2));
    }

    @Test
    public void shouldEndGameWhenLastMatchRemoved() throws GameAlreadyEndedException, InvalidAmountToRemoveException {
        // given
        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(1), GameStatus.PLAYER_ONE_TURN, CPUDifficulty.EASY);

        // when
        nimGame.removeMatches(1);

        // then
        assertThat(nimGame.gameEnded()).isTrue();
    }

}