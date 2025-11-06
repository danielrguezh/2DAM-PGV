package com.docencia.ejercicios;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.docencia.ejrecicios.FabricaDroids;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class FabricaDroidsTest {

    @Test
    void FabricaDroidsNoSeActivaAntesDeEnsamblaryCuentaCorrecta() {
        ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salidaCapturada));

        FabricaDroids fab = new FabricaDroids();
        fab.main();

        String salida = salidaCapturada.toString();

        for (int k = 1; k <= fab.getN(); k++) {
            String ensamblado = "Ensamblado Droid-" + k;
            String activado = "Activado Droid-" + k;

            int idxE = salida.indexOf(ensamblado);
            int idxA = salida.indexOf(activado);

            assertTrue(idxE != -1, "Debe haberse ensamblado " + ensamblado);
            assertTrue(idxA != -1, "Debe haberse activado " + activado);
            assertTrue(idxE < idxA, "El " + activado + " no puede ocurrir antes de " + ensamblado);
        }

        assertEquals(fab.getN(), fab.getActivados(),
                "Debe haberse activado exactamente N droids");
    }
}
