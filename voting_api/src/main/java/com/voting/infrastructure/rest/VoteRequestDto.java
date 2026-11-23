package com.voting.infrastructure.rest;

import jakarta.validation.constraints.NotBlank;

/** DTO de entrada HTTP para la recepción de un voto. */
public record VoteRequestDto(
        @NotBlank(message = "El voterId es obligatorio") String voterId,
        @NotBlank(message = "El candidateId es obligatorio") String candidateId,
        @NotBlank(message = "El regionId es obligatorio") String regionId
) {}