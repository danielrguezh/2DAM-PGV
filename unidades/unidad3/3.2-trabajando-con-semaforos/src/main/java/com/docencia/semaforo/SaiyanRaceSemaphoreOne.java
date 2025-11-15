package com.docencia.semaforo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class SaiyanRaceSemaphoreOne implements Runnable {
    private final String name;
    private int distance = 0;

    private static final int GOAL = 100;

    private static final Semaphore semaphore = new Semaphore(1, true);

    private static final AtomicBoolean winnerDeclared = new AtomicBoolean(false);

    public SaiyanRaceSemaphoreOne(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            while (!winnerDeclared.get() && distance < GOAL) {

                semaphore.acquire();
                try {
                    if (winnerDeclared.get()) {
                        break;
                    }
                    int step = ThreadLocalRandom.current().nextInt(1, 11);
                    distance += step;
                    System.out.println(name + " avanzó " + step + " metros. Distancia total: " + distance + " metros.");

                    if (distance >= GOAL) {
                        if (winnerDeclared.compareAndSet(false, true)) {
                            System.out.println(name + " ha ganado la carrera!");
                        }
                    }
                } finally {
                    semaphore.release();
                }
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 401));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread goku = new Thread(new SaiyanRaceSemaphoreOne("Goku"));
        Thread vegeta = new Thread(new SaiyanRaceSemaphoreOne("Vegeta"));

        goku.start();
        vegeta.start();

        goku.join();
        vegeta.join();
    }
}