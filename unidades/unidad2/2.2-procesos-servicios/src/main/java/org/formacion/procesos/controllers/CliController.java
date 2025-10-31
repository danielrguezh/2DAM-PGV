
package org.formacion.procesos.controllers;

import java.util.Scanner;

import org.formacion.procesos.services.impl.LsofServiceImpl;
import org.formacion.procesos.services.impl.PsServiceImpl;
import org.formacion.procesos.services.impl.TopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danielrguezh
 * @version 1.0.0
 */

@Service
public class CliController {
    private PsServiceImpl comandoPsController;
    private LsofServiceImpl comandoLsofController;
    private TopServiceImpl comandoTopContoller;

    @Autowired
    public void setComandoPsController(PsServiceImpl comandoPsController) {
        this.comandoPsController = comandoPsController;
    }

    @Autowired
    public void setComandoLsofController(LsofServiceImpl comandoLsofController) {
        this.comandoLsofController = comandoLsofController;
    }

    @Autowired
    public void setComandoTopContoller(TopServiceImpl comandoTopContoller) {
        this.comandoTopContoller = comandoTopContoller;
    }

    /**
     * Menu para mostrar en la consola los comandos que puede lanzar el usuario
     * y leer ese comado introducido
     */
    public void menuConsola() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Lanzador de Procesos (CLI) Linux ===\n" +
                "Comandos:\n" +
                "  lsof -i\n" +
                "  top\n" +
                "  ps aux | head \n");
        String linea = scanner.nextLine();
        if (linea.toUpperCase().startsWith("PS")) {
            comandoPsController.procesarLinea(linea);
        }
        if (linea.toUpperCase().startsWith("TOP")) {
            comandoTopContoller.procesarLinea(linea);
        }
        if (linea.toUpperCase().startsWith("LSOF")) {
            comandoLsofController.procesarLinea(linea);
        }
        scanner.close();
    }
}
