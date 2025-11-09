package com.docencia.ejrecicios;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author danielrguezh
 * @version 1.0.0
 */
public class BatallaPokemon {

    private final AtomicBoolean juegoTerminado = new AtomicBoolean(false);
    private int hpPikachu = 100;
    private int hpCharmander = 100;
    private String turno = "Pikachu";

    private final Lock m = new ReentrantLock();
    private final Condition turnoCambio = m.newCondition();

    private void atacar(String atacante, boolean esPikachu) {
        int daño = ThreadLocalRandom.current().nextInt(5, 21);
        if (esPikachu) {
            hpCharmander -= daño;
            System.out.println(atacante + " ataca con " + daño + " de daño. HP rival: " + hpCharmander);
            if (hpCharmander <= 0 && !juegoTerminado.get()) {
                juegoTerminado.set(true);
                System.out.println(atacante + " ha ganado la batalla!");
            }
        } else {
            hpPikachu -= daño;
            System.out.println(atacante + " ataca con " + daño + " de daño. HP rival: " + hpPikachu);
            if (hpPikachu <= 0 && !juegoTerminado.get()) {
                juegoTerminado.set(true);
                System.out.println(atacante + " ha ganado la batalla!");
            }
        }

        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(200, 601));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private final Runnable hiloPikachu = () -> {
        while (!juegoTerminado.get()) {
            m.lock();
            try {
                while (!"Pikachu".equals(turno) && !juegoTerminado.get()) {
                    turnoCambio.await();
                }
                if (juegoTerminado.get()) break;
                atacar("Pikachu", true);
                turno = "Charmander";
                turnoCambio.signal();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } finally {
                m.unlock();
            }
        }
    };

    private final Runnable hiloCharmander = () -> {
        while (!juegoTerminado.get()) {
            m.lock();
            try {
                while (!"Charmander".equals(turno) && !juegoTerminado.get()) {
                    turnoCambio.await();
                }
                if (juegoTerminado.get()) break;
                atacar("Charmander", false);
                turno = "Pikachu";
                turnoCambio.signal();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } finally {
                m.unlock();
            }
        }
    };

    public void main() {
        Thread t1 = new Thread(hiloPikachu);
        Thread t2 = new Thread(hiloCharmander);
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public static void main(String[] args) {
        
    }

    // Getters
    public boolean isJuegoTerminado() {
        return juegoTerminado.get();
    }

    public int getHpPikachu() {
        return hpPikachu;
    }

    public int getHpCharmander() {
        return hpCharmander;
    }
}
