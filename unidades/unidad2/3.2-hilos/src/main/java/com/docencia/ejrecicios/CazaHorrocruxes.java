package com.docencia.ejrecicios;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author danielrguezh
 * @version 1.0.0
 */
public class CazaHorrocruxes {

    private final AtomicBoolean encontrado = new AtomicBoolean(false);
    private final AtomicReference<String> ganador = new AtomicReference<>(null);

    private Runnable Buscador(String nombre, String ubicacion) {
        return () -> {
            int tiempo = ThreadLocalRandom.current().nextInt(500, 2001);
            try {
                Thread.sleep(tiempo);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (!encontrado.get()) {
                // Sincronizamos para asegurar que solo uno gana
                synchronized (this) {
                    if (!encontrado.get()) {
                        encontrado.set(true);
                        ganador.set(nombre);
                        System.out.println(nombre + " encontró un Horrocrux en " + ubicacion + ". ¡Búsqueda terminada!");
                    }
                }
            }
        };
    }

    public void main() {
        Thread t1 = new Thread(Buscador("Harry", "Bosque Prohibido"));
        Thread t2 = new Thread(Buscador("Hermione", "Biblioteca Antigua"));
        Thread t3 = new Thread(Buscador("Ron", "Mazmorras del castillo"));

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
    }

    // Getters para el test
    public boolean isEncontrado() {
        return encontrado.get();
    }

    public String getGanador() {
        return ganador.get();
    }
}
