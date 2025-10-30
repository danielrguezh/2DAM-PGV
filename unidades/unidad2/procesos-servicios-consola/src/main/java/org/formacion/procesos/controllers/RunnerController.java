package org.formacion.procesos.controllers;

import java.util.Scanner;

import org.formacion.procesos.services.ComandoLsService;
import org.formacion.procesos.services.ComandoPsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RunnerController {

    @Autowired
    ComandoPsService comandoPsController;

    @Autowired
    ComandoLsService comandoLsController;

    public void menuConsola() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Lanzador de Procesos (CLI) Linux/Windows ===\n" +
                "Comandos:\n" +
                "  run PING host=8.8.8.8 count=4 timeoutMs=15000\n" +
                "  run LIST_DIR path=.\n" +
                "  run HASH_SHA256 file=README.md\n" +
                "  help | os | exit\n");
        String linea = scanner.nextLine();
        if (linea.toUpperCase().startsWith("PS")) {
            comandoPsController.procesarLinea(linea);
        }
        comandoLsController.procesarLinea(linea);
    }

    private void helpConsola() {
        System.out.println(
                "Ejemplos\n" +
                        "run PING host=8.8.8.8 count=4\n" +
                        "run LIST_DIR path=.\n" +
                        "run HASH_SHA256 file=README.md timeoutMs=5000\n");
    }
}
