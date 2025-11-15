package com.docencia.semaforo.ejercicios;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class EstudianteMejorado extends Thread {

    private final String nombre;
    private final Semaphore semaforo;

    public EstudianteMejorado(String nombre, Semaphore semaforo) {
        this.nombre = nombre;
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        try {
            semaforo.acquire();

            // Determinar qué equipo se está utilizando
            int equipo = semaforo.availablePermits() + 1;

            System.out.println(nombre + " ha comenzado a utilizar el equipo " + equipo + ".");

            Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5001));

            System.out.println(nombre + " ha finalizado con el equipo " + equipo + ".");

            semaforo.release();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        Semaphore equipos = new Semaphore(4);

        for (int i = 1; i <= 6; i++) {
            new EstudianteMejorado("Estudiante " + i, equipos).start();
        }
    }
}

