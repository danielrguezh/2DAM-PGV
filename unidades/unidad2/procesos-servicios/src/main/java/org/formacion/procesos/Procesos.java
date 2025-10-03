package org.formacion.procesos;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author danielrguezh
 * @version 1.0.0
 */
import org.springframework.stereotype.Component;


@Component
public class Procesos {

    private final static String PATH = "src/main/resources/mis_procesos.txt";


    public void ejecutar() {
        File archivo = new File(PATH);
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
            guardarProcesosJava();
            mostrarNumeroLineasArchivo(archivo.getPath());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    /**
     * Funcion que guarda los procesos encontrados de java en un archivo txt
     */
    private void guardarProcesosJava() {
        ProcessBuilder processBuilder;
        processBuilder = new ProcessBuilder( "sh", "-c", "ps aux | grep java > " + PATH);
        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Funcion para mostrar cuantas lineas tiene un archivo, si hay mas de 3 procesos,
     * imprime la frase "¡Cuidado, muchos procesos de Java activos!"
     */
    private void mostrarNumeroLineasArchivo(String nombreArchivo) {
        try {
            ProcessBuilder processBuilder;
            processBuilder = new ProcessBuilder("sh", "-c", "wc -l \"" + nombreArchivo + "\"");
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String linea = reader.readLine();
                if (linea != null && !linea.isBlank()) {
                    int numeroLineas;
                    String[] partes = linea.trim().split("\\s+");
                    numeroLineas = Integer.parseInt(partes[0]);
                    System.out.println("Numero de procesos registrados en el archivo " + nombreArchivo + "= " + numeroLineas);
                    if (numeroLineas > 3) {
                        System.out.println("¡Cuidado, muchos procesos de Java activos!");
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("No se ha podido contar los procesos: " + e.getMessage());
            e.printStackTrace();
        }

    }
}