package com.docencia.ejercicios;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.docencia.ejrecicios.Quidditch;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class QuidditchTest {

    @Test
    void Quidditch_terminaCuandoSnitchAtrapada() {
        ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salidaCapturada));

        Quidditch q = new Quidditch();
        q.main();

        String salida = salidaCapturada.toString();

        assertTrue(salida.contains("¡Snitch dorada atrapada!"),
                "Debe aparecer mensaje de que la snitch fue atrapada");
        assertTrue(salida.contains("Marcador final:"),
                "Debe imprimirse el marcador final");
    }
}
