package com.docencia.ejercicios;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.docencia.ejrecicios.ExploradoresJedi;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExploradoresJediTest {

    @Test
    void ExploradoresJediUnSoloGanador() {
        // Capturar salida estándar
        ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salidaCapturada));

        ExploradoresJedi e = new ExploradoresJedi();
        e.main();

        String salida = salidaCapturada.toString();

        assertTrue(e.isPistaEncontrada(), "Debe haberse encontrado una pista");
        long ocurrencias = salida.split("halló una pista", -1).length - 1;
        assertEquals(1, ocurrencias, "Debe haber exactamente un hallazgo en la salida");
    }
}
