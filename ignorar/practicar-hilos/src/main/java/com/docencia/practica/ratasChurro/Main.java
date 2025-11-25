package com.docencia.practica.ratasChurro;

public class Main {
 public static void main(String[] args) {
        PleandosePorChurro pelea = new PleandosePorChurro();
        LinkinPark musica = new LinkinPark("linkinPark.txt");

        Thread hiloPelea = new Thread(() -> pelea.main());
        Thread hiloMusica = new Thread(() -> musica.iniciarLectura());

        hiloPelea.start();
        hiloMusica.start();

        try {
            hiloPelea.join();
            hiloMusica.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nLa pelea ha terminado... y la música también.");
    }
}
