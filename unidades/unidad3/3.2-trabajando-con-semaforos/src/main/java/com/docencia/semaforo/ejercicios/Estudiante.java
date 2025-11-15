package com.docencia.semaforo.ejercicios;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Estudiante extends Thread {

    private final String nombre;
    private final Semaphore semaforo;

    public Estudiante(String nombre, Semaphore semaforo) {
        this.nombre = nombre;
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        try {
            // Esperar a un equipo libre
            semaforo.acquire();

            System.out.println(nombre + " ha comenzado a utilizar el equipo.");

            Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5001));

            System.out.println(nombre + " ha finalizado con el equipo.");

            semaforo.release();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        Semaphore equipos = new Semaphore(4); // 4 equipos disponibles

        for (int i = 1; i <= 6; i++) {
            new Estudiante("Estudiante " + i, equipos).start();
        }
    }
}
