package com.example.nim.infrastructure.rest;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class RemoveMatchesResource {
    @Getter
    @Setter
    @Min(value = 1)
    private int matchesToRemove;
}
