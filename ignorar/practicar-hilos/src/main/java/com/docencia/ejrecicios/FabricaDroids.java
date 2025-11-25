    package com.docencia.ejrecicios;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author danielrguezh
 * @version 1.0.0
 */
public class FabricaDroids {

    private final BlockingQueue<String> ensamblados = new LinkedBlockingQueue<>();
    private final int N = 10;
    private final AtomicInteger activados = new AtomicInteger(0);

    private final Runnable Ensamblador = () -> {
        for (int i = 1; i <= N; i++) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 301));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            String d = "Droid-" + i;
            System.out.println("Ensamblado " + d);
            try {
                ensamblados.put(d);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    };

    private final Runnable Activador = () -> {
        int cuenta = 0;
        while (cuenta < N) {
            try {
                String d = ensamblados.take();
                System.out.println("Activado " + d);
                activados.incrementAndGet();
                cuenta++;
                Thread.sleep(ThreadLocalRandom.current().nextInt(50, 151));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    };

    public void main() {
        Thread tE = new Thread(Ensamblador);
        Thread tA = new Thread(Activador);
        tE.start();
        tA.start();
        try {
            tE.join();
            tA.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Getters
    public int getN() {
        return N;
    }

    public int getActivados() {
        return activados.get();
    }
}
