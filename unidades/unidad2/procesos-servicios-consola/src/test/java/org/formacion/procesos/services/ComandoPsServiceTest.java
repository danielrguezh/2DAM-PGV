package org.formacion.procesos.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ComandoPsServiceTest {
    ComandoPsService comandoPsService;

    @BeforeEach
    void beforeEach() {
        comandoPsService = new ComandoPsService();
        comandoPsService.setComando("ps");
    }

    @Test
    void validarXaTest() {
        String[] arrayComando = {"ps", "xa"};
        boolean valida = comandoPsService.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarXaMenosTest() {
        String[] arrayComando = {"ps", "-xa"};
        boolean valida = comandoPsService.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarMenosVacioTest() {
        String[] arrayComando = {"ps", "-"};
        boolean valida = comandoPsService.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarVacioTest() {
        String[] arrayComando = {"ps", " "};
        boolean valida = comandoPsService.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarSinVacioTest() {
        String[] arrayComando = {"ps", ""};
        boolean valida = comandoPsService.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarFalseSinGuionTest() {
        String[] arrayComando = {"ps", "lalala"};
        boolean valida = comandoPsService.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarFalseTest() {
        String[] arrayComando = {"ps", "-lalala"};
        boolean valida = comandoPsService.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }
}
