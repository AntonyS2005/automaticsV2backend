package com.automatics.demo;

import java.util.List;

public record RespuestaValidacion(
        boolean esValido,
        List<String> errores
) {}