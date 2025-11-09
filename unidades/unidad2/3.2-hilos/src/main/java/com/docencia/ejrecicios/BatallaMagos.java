package com.docencia.ejrecicios;

import java.util.Random;

public class BatallaMagos {

    private int energiaGandalf = 120;
    private int energiaSaruman = 120;
    private volatile boolean combateTerminado = false;
    private final Object lock = new Object(); 
    private final Random random = new Random();

    private void lanzarHechizo(String atacante, boolean atacaGandalf) {
        int daño = random.nextInt(18) + 8; // [8,25]
        if (atacaGandalf) {
            energiaSaruman -= daño;
            System.out.println(atacante + " lanza hechizo por " + daño +
                    ". Energía rival: " + energiaSaruman);
            if (energiaSaruman <= 0 && !combateTerminado) {
                combateTerminado = true;
                System.out.println(atacante + " gana la batalla mágica.");
            }
        } else {
            energiaGandalf -= daño;
            System.out.println(atacante + " lanza hechizo por " + daño +
                    ". Energía rival: " + energiaGandalf);
            if (energiaGandalf <= 0 && !combateTerminado) {
                combateTerminado = true;
                System.out.println(atacante + " gana la batalla mágica.");
            }
        }
    }

    private final Runnable Gandalf = () -> {
        while (!combateTerminado) {
            synchronized (lock) {
                if (!combateTerminado) lanzarHechizo("Gandalf", true);
            }
            dormir(random.nextInt(401) + 200); // [200,600] ms
        }
    };

    private final Runnable Saruman = () -> {
        while (!combateTerminado) {
            synchronized (lock) {
                if (!combateTerminado) lanzarHechizo("Saruman", false);
            }
            dormir(random.nextInt(401) + 200); // [200,600] ms
        }
    };

    public void main() {
        Thread t1 = new Thread(Gandalf);
        Thread t2 = new Thread(Saruman);
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //Getters
    public boolean isCombateTerminado() { return combateTerminado; }
    public int getEnergiaGandalf() { return energiaGandalf; }
    public int getEnergiaSaruman() { return energiaSaruman; }
}
