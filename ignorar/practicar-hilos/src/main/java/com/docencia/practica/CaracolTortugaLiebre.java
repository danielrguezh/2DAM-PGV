package com.docencia.practica;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CaracolTortugaLiebre implements Runnable{
    private String name;
    private int distance = 0;
    private static final int GOAL = 1000;
    private static volatile boolean winnerDeclared = false;

    private int vMaxima;
    private int vMinima;



    public CaracolTortugaLiebre(String name, int vMaxima, int vMinima) {
        this.name = name;
        this.vMaxima = vMaxima;
        this.vMinima = vMinima;
    }
    


    @Override
    public void run() {
        Random random = new Random();
        while (distance < GOAL && !winnerDeclared) {
            int step=random.nextInt(vMaxima-vMinima+1) + vMinima;
            distance+=step;
            System.out.println(name + " sumó " + step + "u. Distancia recorrida: "+distance+"u.");


            if (distance>=GOAL && !winnerDeclared) {
                winnerDeclared=true;
                System.out.println(name + " ha ganado la carrera.");
            }

            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1, 601));
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Thread caracol = new Thread(new CaracolTortugaLiebre("Caracol", 10, 1));
        Thread tortuga = new Thread(new CaracolTortugaLiebre("Tortuga", 20, 5));
        Thread liebre = new Thread(new CaracolTortugaLiebre("Liebre", 50, 20));
        
        caracol.start();
        tortuga.start();
        liebre.start();
    }

}
