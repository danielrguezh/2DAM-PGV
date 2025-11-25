package com.docencia.practica.ratasChurro;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.*;

public class PleandosePorChurro {
    private final AtomicBoolean pelaTerminada = new AtomicBoolean(false);
    private int vidaRata1= 100;
    private int vidaRata2= 100;
    private String turno = "rata1";

    private final Lock m = new ReentrantLock();
    private final Condition turnoCambio = m.newCondition();

    private void atacar(String atacante, boolean esRata1){
        int danio = ThreadLocalRandom.current().nextInt(5,15);
        if (esRata1) {
            vidaRata2 -= danio;
            System.out.println(atacante+ " ataca con "+danio+ " de danio. Vida de la rata 2 = "+vidaRata2);
            if (vidaRata2 <=0 && !pelaTerminada.get()) {
                pelaTerminada.set(true);
                System.out.println("------------------------------");
                System.out.println("| "+atacante + " ha ganado el churro |");
                System.out.println("------------------------------");
            }
        }else{
            vidaRata1 -= danio;
            System.out.println(atacante+ " ataca con "+danio+ " de danio. Vida de la rata 1 = "+vidaRata1);
            if (vidaRata2 <=0 && !pelaTerminada.get()) {
                pelaTerminada.set(true);
                System.out.println("------------------------------");
                System.out.println("| "+atacante + " ha ganado el churro |");
                System.out.println("------------------------------");
            }
        }

        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(200,600));
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    private final Runnable hiloRata1()  {
        return () -> {
            while (!pelaTerminada.get()) {
                m.lock();
                try {
                    while (!"rata1".equals(turno) && !pelaTerminada.get()) {
                        turnoCambio.await();
                    }
                    if (pelaTerminada.get()) break; 
                    atacar("rata1", true);
                    turno="rata2";
                    turnoCambio.signal();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }finally{
                    m.unlock();
                }
            }
        };
    }

    private final Runnable hiloRata2()  {
        return () -> {
            while (!pelaTerminada.get()) {
                m.lock();
                try {
                    while (!"rata2".equals(turno) && !pelaTerminada.get()) {
                        turnoCambio.await();
                    }
                    if (pelaTerminada.get()) break; 
                    atacar("rata2", false);
                    turno="rata1";
                    turnoCambio.signal();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }finally{
                    m.unlock();
                }
            }
        };
    }

    public void main() {
        Thread t1 = new Thread(hiloRata1());
        Thread t2 = new Thread(hiloRata2());
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
