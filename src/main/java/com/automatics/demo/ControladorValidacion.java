package com.automatics.demo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ControladorValidacion {

  @Autowired
  private ServicioValidacion servicioValidacion;

  @PostMapping("/validar")
  public ResponseEntity<RespuestaValidacion> validarTexto(
          @Valid @RequestBody PeticionValidacion peticion
  ) {
    List<String> errores = servicioValidacion.validar(peticion.texto());

    if (errores.isEmpty()) {
      return ResponseEntity.ok(new RespuestaValidacion(true, List.of()));
    } else {
      return ResponseEntity.badRequest().body(new RespuestaValidacion(false, errores));
    }
  }
}