package com.docencia.ejrecicios;
import java.util.Random;

public class MilleniumFalcon {

    private volatile boolean fin = false;
    private volatile boolean destruida = false;

    private final int tiempoMisionMS = 4000;
    private long inicio;

    private volatile int velocidad = 0;
    private volatile int escudos = 100;

    private final Random random = new Random();

    private final Runnable HanSolo = () -> {
        while (!fin) {
            velocidad += random.nextInt(11) + 5; // [5,15]
            if (random.nextInt(100) < 5) { // 5% de probabilidad
                destruida = true;
                fin = true;
                System.out.println("Fallo de hiperimpulsor. ¡La nave se destruye!");
            }
            dormir(150);
            if (System.currentTimeMillis() - inicio >= tiempoMisionMS) {
                fin = true;
            }
        }
    };

    private final Runnable Chewbacca = () -> {
        while (!fin) {
            escudos += random.nextInt(16) - 10; // [-10, +5]
            if (escudos <= 0) {
                destruida = true;
                fin = true;
                System.out.println("¡Escudos agotados! ¡La nave se destruye!");
            }
            dormir(150);
            if (System.currentTimeMillis() - inicio >= tiempoMisionMS) {
                fin = true;
            }
        }
    };

    public void main() {
        inicio = System.currentTimeMillis();
        Thread t1 = new Thread(HanSolo);
        Thread t2 = new Thread(Chewbacca);
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!destruida) {
            System.out.println("¡Han y Chewie escapan! Vel=" + velocidad + ", Escudos=" + escudos);
        }
    }

    private void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
