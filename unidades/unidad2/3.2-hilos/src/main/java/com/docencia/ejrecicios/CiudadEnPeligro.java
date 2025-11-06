package com.docencia.ejrecicios;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CiudadEnPeligro {

    private final AtomicBoolean amenazaNeutralizada = new AtomicBoolean(false);
    private final List<String> zonasA = Arrays.asList("Norte", "Centro", "Este");
    private final List<String> zonasB = Arrays.asList("Oeste", "Sur");
    private final AtomicReference<String> ganador = new AtomicReference<>(null);

    private final Runnable Superman = () -> {
        for (String zona : zonasA) {
            if (amenazaNeutralizada.get()) break;
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(200, 501));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("Superman salvó " + zona);
        }

        if (!amenazaNeutralizada.get()) {
            synchronized (this) {
                if (!amenazaNeutralizada.get()) {
                    amenazaNeutralizada.set(true);
                    ganador.set("Superman");
                    System.out.println("Amenaza neutralizada por Superman");
                }
            }
        }
    };

    private final Runnable Batman = () -> {
        for (String zona : zonasB) {
            if (amenazaNeutralizada.get()) break;
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(300, 601));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("Batman salvó " + zona);
        }

        if (!amenazaNeutralizada.get()) {
            synchronized (this) {
                if (!amenazaNeutralizada.get()) {
                    amenazaNeutralizada.set(true);
                    ganador.set("Batman");
                    System.out.println("Amenaza neutralizada por Batman");
                }
            }
        }
    };

    public void main() {
        Thread t1 = new Thread(Superman);
        Thread t2 = new Thread(Batman);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Getters para el test
    public boolean isAmenazaNeutralizada() {
        return amenazaNeutralizada.get();
    }

    public String getGanador() {
        return ganador.get();
    }
}
