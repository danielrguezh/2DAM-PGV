package com.docencia.ejercicios;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.docencia.ejrecicios.BatallaMagos;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class BatallaMagosTest {

    @Test
    void BatallaMagos_debeHaberGanadorYTerminar() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BatallaMagos b = new BatallaMagos();
        b.main();

        String salida = output.toString();

        assertTrue(salida.contains("gana la batalla mágica."),"Debe anunciar un ganador.");
        assertTrue(b.isCombateTerminado(),"El combate debe marcarse como terminado.");
        assertTrue(b.getEnergiaGandalf() <= 0 || b.getEnergiaSaruman() <= 0, "Uno de los magos debe tener energía <= 0.");
    }
}

