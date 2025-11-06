package com.docencia.ejrecicios;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FuerzaThorHulk {

    private final int durationMS = 5000;
    private final AtomicBoolean tiempoTerminado = new AtomicBoolean(false);
    private final AtomicInteger totalThor = new AtomicInteger(0);
    private final AtomicInteger totalHulk = new AtomicInteger(0);

    private final Runnable Temporizador = () -> {
        try {
            Thread.sleep(durationMS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        tiempoTerminado.set(true);
        System.out.println("¡Tiempo!");
    };

    private final Runnable Thor = () -> {
        while (!tiempoTerminado.get()) {
            int peso = ThreadLocalRandom.current().nextInt(5, 21);
            totalThor.addAndGet(peso);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(50, 121));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    };

    private final Runnable Hulk = () -> {
        while (!tiempoTerminado.get()) {
            int peso = ThreadLocalRandom.current().nextInt(5, 21);
            totalHulk.addAndGet(peso);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(50, 121));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    };

    public void main() {
        Thread t0 = new Thread(Temporizador);
        Thread t1 = new Thread(Thor);
        Thread t2 = new Thread(Hulk);

        t0.start();
        t1.start();
        t2.start();

        try {
            t0.join();
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int thor = totalThor.get();
        int hulk = totalHulk.get();

        if (thor > hulk) {
            System.out.println("Thor gana con " + thor + " vs " + hulk);
        } else if (hulk > thor) {
            System.out.println("Hulk gana con " + hulk + " vs " + thor);
        } else {
            System.out.println("Empate: " + thor);
        }
    }

    public boolean isTiempoTerminado() {
        return tiempoTerminado.get();
    }

    public int getTotalThor() {
        return totalThor.get();
    }

    public int getTotalHulk() {
        return totalHulk.get();
    }
}
