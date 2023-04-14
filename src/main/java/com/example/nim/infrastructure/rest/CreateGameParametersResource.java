package com.example.nim.infrastructure.rest;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateGameParametersResource {
    @NotNull
    private DiffcultyResource difficulty;
    @NotNull
    private boolean playerOneBegins;
}
