package com.docencia.ejercicios;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.docencia.ejrecicios.CiudadEnPeligro;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CiudadEnPeligroTest {

    @Test
    void CiudadEnPeligroSoloNeutralizaElOtroSeDetiene() {
        ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salidaCapturada));

        CiudadEnPeligro c = new CiudadEnPeligro();
        c.main();

        String salida = salidaCapturada.toString();

        assertTrue(c.isAmenazaNeutralizada(), "Debe haberse neutralizado la amenaza");
        assertTrue(
            c.getGanador().equals("Superman") || c.getGanador().equals("Batman"),
            "El ganador debe ser Superman o Batman"
        );

        long ocurrencias = salida.split("Amenaza neutralizada", -1).length - 1;
        assertEquals(1, ocurrencias, "Solo debe imprimirse una neutralización");
    }
}
