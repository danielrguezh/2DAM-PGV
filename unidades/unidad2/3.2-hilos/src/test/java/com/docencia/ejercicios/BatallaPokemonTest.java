package com.docencia.ejercicios;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.docencia.ejrecicios.BatallaPokemon;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BatallaPokemonTest {

    @Test
    void BatallaPokemon_debeHaberGanador() {
        ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salidaCapturada));

        BatallaPokemon juego = new BatallaPokemon();
        juego.main();

        String salida = salidaCapturada.toString();

        assertTrue(salida.contains("ha ganado la batalla!"),"Debe aparecer mensaje de victoria");
        assertTrue(juego.isJuegoTerminado(), "El juego debe haber terminado");
        assertTrue(juego.getHpPikachu() <= 0 || juego.getHpCharmander() <= 0, "Al menos uno debe tener HP <= 0");
    }
}

