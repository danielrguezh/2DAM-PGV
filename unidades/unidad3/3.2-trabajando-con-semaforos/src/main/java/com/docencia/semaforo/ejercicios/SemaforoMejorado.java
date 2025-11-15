package com.docencia.semaforo.ejercicios;

import java.util.concurrent.Semaphore;

public class SemaforoMejorado {

    private static class ColorThread extends Thread {

        private final String color;
        private final long tiempoEspera;

        private final Semaphore miTurno;
        private final Semaphore siguienteTurno;

        private volatile boolean running = true;

        public ColorThread(String color, long tiempoEspera,Semaphore miTurno, Semaphore siguienteTurno) {
            this.color = color;
            this.tiempoEspera = tiempoEspera;
            this.miTurno = miTurno;
            this.siguienteTurno = siguienteTurno;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    miTurno.acquire();

                    if (!running) {
                        siguienteTurno.release();
                        break;
                    }

                    System.out.println("COLOR: " + color);
                    Thread.sleep(tiempoEspera);

                    siguienteTurno.release();

                } catch (InterruptedException e) {
                    running = false;
                    siguienteTurno.release();
                }
            }
        }

        public void detener() {
            running = false;
            miTurno.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Semaphore turnoRojo = new Semaphore(1);
        Semaphore turnoVerde = new Semaphore(0);
        Semaphore turnoAmbar = new Semaphore(0);

        ColorThread rojo  = new ColorThread("ROJO",   3000, turnoRojo, turnoVerde);
        ColorThread verde = new ColorThread("VERDE",  3000, turnoVerde, turnoAmbar);
        ColorThread ambar = new ColorThread("ÁMBAR",  1000, turnoAmbar, turnoRojo);

        rojo.start();
        verde.start();
        ambar.start();

        Thread.sleep(20_000);

        rojo.detener();
        verde.detener();
        ambar.detener();

        rojo.join();
        verde.join();
        ambar.join();

        System.out.println("Simulación finalizada.");
    }
}
