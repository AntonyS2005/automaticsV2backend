package com.automatics.demo;

import jakarta.validation.constraints.NotBlank;

public record PeticionValidacion(
        @NotBlank(message = "El campo 'texto' no puede estar vacío.")
        String texto
) {}