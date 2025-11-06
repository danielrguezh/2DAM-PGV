package com.docencia.hilos;

public class MyThread extends Thread{
    @Override
    public void run() {
        System.out.println("Soy un hilo desde MyThread");
    }
}
