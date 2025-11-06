package com.docencia.hilos;

public class MiRunnable implements Runnable{

    @Override
    public void run() {
        System.out.println("Soy un hilo desde Runnable");
    }

}
