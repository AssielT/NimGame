package com.example.nim.infrastructure.rest;

import com.example.nim.NimApplication;
import com.example.nim.application.CreateGameParameter;
import com.example.nim.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.lessThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {NimApplication.class})
@WebAppConfiguration
class NimGameControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(13), GameStatus.PLAYER_ONE_TURN, CPUDifficulty.EASY);
        gameRepository.save(nimGame);
    }

    @Test
    void givenNoGameExist_whenGetGameState_thenReturnNotFound() throws Exception {
        this.mockMvc.perform(get("/misereGame/notExistingId")).andExpect(status().isNotFound());
    }

    @Test
    void givenGameExistWithId1_whenGetGameState_thenReturnGameState() throws Exception {
        this.mockMvc.perform(get("/misereGame/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.gameId").value("1"))
                .andExpect(jsonPath("$.status").value("PLAYER_ONE_TURN"))
                .andExpect(jsonPath("$.matchesLeft").value(13));
    }

    @Test
    void givenGameExistWithId1_whenPostTakeMatches_thenReturnGameState() throws Exception {
        RemoveMatchesResource removeMatchesResource = new RemoveMatchesResource();
        removeMatchesResource.setMatchesToRemove(2);

        this.mockMvc.perform(post("/misereGame/1").content(asJsonString(removeMatchesResource)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.gameId").value("1"))
                .andExpect(jsonPath("$.status").value("PLAYER_ONE_TURN"))
                .andExpect(jsonPath("$.matchesLeft").value(lessThan(11)));
    }

    @Test
    void givenGameId2DontExist_whenPostTakeMatches_thenReturnNotFound() throws Exception {
        RemoveMatchesResource removeMatchesResource = new RemoveMatchesResource();
        removeMatchesResource.setMatchesToRemove(2);

        this.mockMvc.perform(post("/misereGame/2").content(asJsonString(removeMatchesResource)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenGameHasOnlyOneMatchLeft_whenPostTakeMatchesWithMoreThanOneMatch_thenReturnUnprocessableEntity() throws Exception {
        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(1), GameStatus.PLAYER_ONE_TURN, CPUDifficulty.EASY);
        gameRepository.save(nimGame);

        RemoveMatchesResource removeMatchesResource = new RemoveMatchesResource();
        removeMatchesResource.setMatchesToRemove(2);

        this.mockMvc.perform(post("/misereGame/1").content(asJsonString(removeMatchesResource)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void givenGameHasEnded_whenPostTakeMatches_thenReturnConflict() throws Exception {
        NimGame nimGame = new NimGame(new GameId("1"), new MatchPile(0), GameStatus.PLAYER_ONE_WON, CPUDifficulty.EASY);
        gameRepository.save(nimGame);

        RemoveMatchesResource removeMatchesResource = new RemoveMatchesResource();
        removeMatchesResource.setMatchesToRemove(2);

        this.mockMvc.perform(post("/misereGame/1").content(asJsonString(removeMatchesResource)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void givenPlayerOneBegins_whenPostStartGame_thenReturnGameState() throws Exception {
        CreateGameParameter createGameParameter = new CreateGameParameter(CPUDifficulty.EASY, true);

        this.mockMvc.perform(post("/misereGame").content(asJsonString(createGameParameter)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.gameId").isNotEmpty())
                .andExpect(jsonPath("$.status").value("PLAYER_ONE_TURN"))
                .andExpect(jsonPath("$.matchesLeft").value(13));
    }

    @Test
    void givenPlayerTwoBegins_whenPostStartGame_thenReturnGameState() throws Exception {
        CreateGameParameter createGameParameter = new CreateGameParameter(CPUDifficulty.EASY, false);

        this.mockMvc.perform(post("/misereGame").content(asJsonString(createGameParameter)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.gameId").isNotEmpty())
                .andExpect(jsonPath("$.status").value("PLAYER_ONE_TURN"))
                .andExpect(jsonPath("$.matchesLeft").value(lessThan(13)));
    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}