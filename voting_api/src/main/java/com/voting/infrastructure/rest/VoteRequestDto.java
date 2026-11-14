package com.voting.infrastructure.rest;

import jakarta.validation.constraints.NotBlank;

public record VoteRequestDto(
        @NotBlank(message = "El voterId es obligatorio") String voterId,
        @NotBlank(message = "El candidateId es obligatorio") String candidateId,
        @NotBlank(message = "El regionId es obligatorio") String regionId
) {}