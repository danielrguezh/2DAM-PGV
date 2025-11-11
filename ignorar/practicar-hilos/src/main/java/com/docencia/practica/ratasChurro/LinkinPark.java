package com.docencia.practica.ratasChurro;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class LinkinPark {

    private final Path rutaArchivo;
    private int indiceActual = 0;

    public LinkinPark(String ruta) {
        this.rutaArchivo = Paths.get(ruta);
    }

    public void iniciarLectura() {
        System.out.println("Buscando archivo en: " + rutaArchivo.toAbsolutePath());

        while (true) {
            try {
                List<String> lineas = Files.readAllLines(rutaArchivo);

                lineas.removeIf(l -> l.trim().isEmpty());

                if (indiceActual < lineas.size()) {
                    String linea = lineas.get(indiceActual);
                    System.out.println( linea);
                    indiceActual++;
                } else {
                    System.out.println("Fin del archivo alcanzado.");
                    break;
                }

                Thread.sleep(1000);
            } catch (IOException e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public static void main(String[] args) {
        LinkinPark lector = new LinkinPark("linkinPark.txt");
        
        lector.iniciarLectura();
    }
}
