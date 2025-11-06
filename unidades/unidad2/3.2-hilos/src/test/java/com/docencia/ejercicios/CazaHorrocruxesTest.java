package com.docencia.ejercicios;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.docencia.ejrecicios.CazaHorrocruxes;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CazaHorrocruxesTest {

    @Test
    void CazaHorrocruxesUnGanadorYUnSoloHallazgo() {
        ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salidaCapturada));

        CazaHorrocruxes sim = new CazaHorrocruxes();
        sim.main();

        String salida = salidaCapturada.toString();

        assertTrue(sim.isEncontrado(), "Debe haberse encontrado un Horrocrux");
        assertTrue(
            sim.getGanador().equals("Harry") ||
            sim.getGanador().equals("Hermione") ||
            sim.getGanador().equals("Ron"),
            "El ganador debe ser Harry, Hermione o Ron"
        );

        long ocurrencias = salida.split("encontró un Horrocrux", -1).length - 1;
        assertEquals(1, ocurrencias, "Debe haber exactamente un hallazgo en la salida");
    }
}
