package com.docencia.ejercicios;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.docencia.ejrecicios.MilleniumFalcon;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class MilleniumFalconTest {

    @Test
    void MilleniumFalcon_finalizaConEscapeODestruccion() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MilleniumFalcon m = new MilleniumFalcon();
        m.main();

        String salida = output.toString();

        boolean destruida = salida.contains("se destruye");
        boolean escapan = salida.contains("escapan");

        assertTrue(destruida ^ escapan, "La salida debe indicar destrucción o escape, pero no ambos. Salida:\n" + salida);
    }
}
