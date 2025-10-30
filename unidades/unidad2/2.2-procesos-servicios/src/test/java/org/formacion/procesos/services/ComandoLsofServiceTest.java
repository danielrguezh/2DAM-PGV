package org.formacion.procesos.services;

import org.formacion.procesos.services.impl.LsofServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ComandoLsofServiceTest {
    LsofServiceImpl comandoLsofService;

    @BeforeEach
    void beforeEach() {
        comandoLsofService = new LsofServiceImpl();
        comandoLsofService.setComando("lsof");
    }

    @Test
    void validarLaTest() {
        String[] arrayComando = {"lsof", "-i"};
        boolean valida = comandoLsofService.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarMenosVacioTest() {
        String[] arrayComando = {"lsof", "-"};
        boolean valida = comandoLsofService.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarVacioTest() {
        String[] arrayComando = {"lsof", " "};
        boolean valida = comandoLsofService.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarSinVacioTest() {
        String[] arrayComando = {"lsof", ""};
        boolean valida = comandoLsofService.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarFalseSinGuionTest() {
        String[] arrayComando = {"lsof", "lalala"};
        boolean valida = comandoLsofService.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarFalseTest() {
        String[] arrayComando = {"lsof", "-lalala"};
        boolean valida = comandoLsofService.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }
}