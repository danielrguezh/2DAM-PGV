package com.docencia.ejrecicios;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Quidditch {

    private final AtomicBoolean snitchAtrapada = new AtomicBoolean(false);
    private int puntosEquipoA = 0;
    private int puntosEquipoB = 0;
    private final Lock m = new ReentrantLock();

    private final Runnable CazadorA = () -> {
        while (!snitchAtrapada.get()) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(200, 501));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            int g = ThreadLocalRandom.current().nextInt(0, 2) * 10;
            if (g > 0) {
                m.lock();
                try {
                    puntosEquipoA += g;
                } finally {
                    m.unlock();
                }
                System.out.println("Equipo A anota 10. Total A=" + puntosEquipoA);
            }
        }
    };

    private final Runnable CazadorB = () -> {
        while (!snitchAtrapada.get()) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(200, 501));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            int g = ThreadLocalRandom.current().nextInt(0, 2) * 10;
            if (g > 0) {
                m.lock();
                try {
                    puntosEquipoB += g;
                } finally {
                    m.unlock();
                }
                System.out.println("Equipo B anota 10. Total B=" + puntosEquipoB);
            }
        }
    };

    private final Runnable Buscador = () -> {
        while (!snitchAtrapada.get()) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(300, 701));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            int chance = ThreadLocalRandom.current().nextInt(1, 101);
            if (chance <= 15) {
                snitchAtrapada.set(true);
                System.out.println("¡Snitch dorada atrapada!");
            }
        }
    };

    public void main() {
        Thread t1 = new Thread(CazadorA);
        Thread t2 = new Thread(CazadorB);
        Thread t3 = new Thread(Buscador);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Marcador final: A=" + puntosEquipoA + " B=" + puntosEquipoB);
    }

    // Getters para posibles pruebas adicionales
    public boolean isSnitchAtrapada() {
        return snitchAtrapada.get();
    }

    public int getPuntosEquipoA() {
        return puntosEquipoA;
    }

    public int getPuntosEquipoB() {
        return puntosEquipoB;
    }
}
