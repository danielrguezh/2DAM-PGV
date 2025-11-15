package com.docencia.semaforo.ejercicios;

public class Semaforo implements Runnable{
public enum Color {
        ROJO, VERDE, AMBAR
    }

    private volatile boolean running = true;
    private Color colorActual = Color.ROJO;

    @Override
    public void run() {
        while (running) {

            switch (colorActual) {

                case ROJO:
                    System.out.println("COLOR: ROJO");
                    dormir(3000);
                    colorActual = Color.VERDE;
                    break;

                case VERDE:
                    System.out.println("COLOR: VERDE");
                    dormir(3000);
                    colorActual = Color.AMBAR;
                    break;

                case AMBAR:
                    System.out.println("COLOR: ÁMBAR");
                    dormir(1000);
                    colorActual = Color.ROJO;
                    break;
            }
        }
        System.out.println("Simulación detenida.");
    }

    private void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            running = false;
        }
    }

    public void detener() {
        running = false;
    }

    public static void main(String[] args) throws InterruptedException {
        Semaforo semaforo = new Semaforo();
        Thread semaforoThread = new Thread(semaforo);
        semaforoThread.start();

        Thread.sleep(20_000);
        semaforo.detener();
        semaforoThread.join();
    }
}