package com.docencia.practica.ratasChurro;

public class Main {
 public static void main(String[] args) {
        // Crear los objetos
        PleandosePorChurro pelea = new PleandosePorChurro();
        LinkinPark musica = new LinkinPark("linkinPark.txt");

        // Crear hilos separados
        Thread hiloPelea = new Thread(() -> pelea.main());
        Thread hiloMusica = new Thread(() -> musica.iniciarLectura());

        // Iniciar ambos
        hiloPelea.start();
        hiloMusica.start();

        // Esperar a que ambos terminen
        try {
            hiloPelea.join();
            hiloMusica.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nLa pelea ha terminado... y la música también.");
    }
}
