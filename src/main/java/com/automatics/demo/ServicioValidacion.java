package com.automatics.demo;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ServicioValidacion {

  private static final Set<Character> EXCEPCIONES_REPETICION = Set.of('o', 'e', 'l', 'c', 'r');
  private static final Set<Character> PUNTUACION_INTERNA_PERMITIDA = Set.of(',', ':', ';');

  public List<String> validar(String cadenaEntrada) {
    Set<String> errores = new HashSet<>();

    if (cadenaEntrada == null || cadenaEntrada.isEmpty()) {
      errores.add("La cadena no puede estar vacía.");
      return new ArrayList<>(errores);
    }

    if (cadenaEntrada.length() < 2) {
      errores.add("La cadena es demasiado corta. Debe empezar con mayúscula o número y terminar con punto (ej. 'A.' o '123.').");
      return new ArrayList<>(errores);
    }

    int longitud = cadenaEntrada.length();
    char primerCaracter = cadenaEntrada.charAt(0);
    char ultimoCaracter = cadenaEntrada.charAt(longitud - 1);

    // Detectar si la cadena contiene letras y números
    boolean contieneLetras = false;
    boolean contieneNumeros = false;

    for (int i = 0; i < longitud - 1; i++) { // Excluimos el punto final
      char c = cadenaEntrada.charAt(i);
      if (esLetraEspanola(c)) {
        contieneLetras = true;
      }
      if (Character.isDigit(c)) {
        contieneNumeros = true;
      }
    }

    // Regla 11: No se pueden mezclar letras y números
    if (contieneLetras && contieneNumeros) {
      errores.add("Regla 11: No se pueden mezclar letras y números en la misma cadena.");
      return new ArrayList<>(errores); // Retornamos inmediatamente si hay mezcla
    }

    // Si es solo números
    if (contieneNumeros && !contieneLetras) {
      if (!Character.isDigit(primerCaracter)) {
        errores.add("Regla 12: Si la cadena contiene números, debe comenzar con un número.");
      }

      if (ultimoCaracter != '.') {
        errores.add("Regla 8: La cadena debe terminar obligatoriamente con un punto (.).");
      }

      // Validar que solo contenga números, puntuación permitida y el punto final
      for (int i = 0; i < longitud - 1; i++) {
        char c = cadenaEntrada.charAt(i);
        if (!Character.isDigit(c) && !esPuntuacionInterna(c)) {
          errores.add("Regla 13: En cadenas numéricas solo se permiten dígitos y puntuación (,:;).");
          break;
        }
      }

      return new ArrayList<>(errores);
    }

    // A partir de aquí, validaciones solo para cadenas con letras
    if (!Character.isUpperCase(primerCaracter) || !esLetraEspanola(primerCaracter)) {
      errores.add("Regla 1: El primer carácter debe ser una letra mayúscula (A-Z, Ñ).");
    }

    if (esPuntuacionInterna(primerCaracter)) {
      errores.add("Regla 4: No se permite puntuación (,:;) en el primer carácter.");
    }

    if (ultimoCaracter != '.') {
      errores.add("Regla 8: La cadena debe terminar obligatoriamente con un punto (.).");
    }

    // Nueva regla: validar que la primera letra no se repita en ninguna parte
    // EXCEPTO si esa letra está en las excepciones de repetición
    char primeraLetraLower = Character.toLowerCase(primerCaracter);

    if (!EXCEPCIONES_REPETICION.contains(primeraLetraLower)) {
      for (int i = 1; i < longitud; i++) {
        char caracterActual = cadenaEntrada.charAt(i);
        char caracterActualLower = Character.toLowerCase(caracterActual);

        if (caracterActualLower == primeraLetraLower && esLetraEspanola(caracterActual)) {
          errores.add("Regla 10: La primera letra '" + primerCaracter + "' no puede repetirse en ninguna posición (ni en mayúscula ni en minúscula).");
          break;
        }
      }
    }

    for (int i = 1; i < longitud; i++) {
      char caracterActual = cadenaEntrada.charAt(i);
      char caracterAnterior = cadenaEntrada.charAt(i - 1);
      boolean esCaracterInterno = (i < (longitud - 1));

      if (esCaracterInterno) {
        if (!Character.isLowerCase(caracterActual) && !esPuntuacionInterna(caracterActual) && esLetraEspanola(caracterActual)) {
          errores.add("Regla 2: El carácter '" + caracterActual + "' debe ser minúscula.");
        }

        if (!esLetraEspanola(caracterActual) && !esPuntuacionInterna(caracterActual)) {
          errores.add("Regla 3: El carácter '" + caracterActual + "' no está permitido. Solo se acepta (,:;).");
        }

        if (caracterActual == '.') {
          errores.add("Regla 9 (Derivada): El punto (.) solo se permite como último carácter.");
        }
      }

      if (caracterActual == caracterAnterior) {
        char lowerActual = Character.toLowerCase(caracterActual);

        if (!EXCEPCIONES_REPETICION.contains(lowerActual)) {
          errores.add("Regla 5: El carácter '" + caracterActual + "' no se puede repetir seguidamente.");
        } else {
          if (i > 1 && cadenaEntrada.charAt(i - 2) == caracterActual) {
            errores.add("Regla 7: El carácter '" + caracterActual + "' solo se puede repetir un máximo de dos veces seguidas.");
          }
        }
      }
    }
    return new ArrayList<>(errores);
  }

  private boolean esLetraEspanola(char c) {
    return Character.isLetter(c) || c == 'ñ' || c == 'Ñ';
  }

  private boolean esPuntuacionInterna(char c) {
    return PUNTUACION_INTERNA_PERMITIDA.contains(c);
  }
}