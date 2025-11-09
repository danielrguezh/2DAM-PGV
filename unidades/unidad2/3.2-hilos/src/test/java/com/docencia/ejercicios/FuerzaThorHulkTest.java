package com.docencia.ejercicios;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.docencia.ejrecicios.FuerzaThorHulk;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class FuerzaThorHulkTest {

    @Test
    void FuerzaThorHulk_terminaPorTiempo_yDeclaraResultado() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        FuerzaThorHulk f = new FuerzaThorHulk();
        f.main();

        String salida = output.toString();
        assertTrue(salida.contains("¡Tiempo!"),"La salida debe indicar que el tiempo terminó.");

        boolean resultadoValido = salida.contains("gana") || salida.contains("Empate");
        assertTrue(resultadoValido,"La salida debe contener 'gana' o 'Empate'.");
        assertTrue(f.isTiempoTerminado(), "El flag de tiempo debe ser verdadero.");
        assertTrue(f.getTotalThor() >= 0 && f.getTotalHulk() >= 0,"Los totales deben ser no negativos.");
    }
}
