package org.formacion.procesos.repositories;

import org.formacion.procesos.repositories.file.FileJobRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FileRepositoryTest {

    private static final String NO_SE_HA_OBTENIDO_EL_RESULTADO_ESPERADO = "No se ha obtenido el resultado esperado";
    static FileJobRepository fileRepository;

    @BeforeAll
    static void beforeAll() {
        fileRepository = new FileJobRepository();
        fileRepository.setFileName("fichero-test.txt");
    }

    @Test
    void addContenido() {
        boolean resultado = fileRepository.add("texto");
        Assertions.assertTrue(resultado, NO_SE_HA_OBTENIDO_EL_RESULTADO_ESPERADO);
    }
}