package com.docencia.hilos;

public class Main2 {
    public static void main(String[] args) {
        MyThread myThread= new MyThread();
        System.out.println("Arrancamos el hilo");
        myThread.start();
        try {
            System.out.println("Me fui a dormir");
            myThread.sleep(5000);
            System.out.println("Me levanto");
        } catch (Exception e) {
            System.out.println("El hilo fue interrumpido");
        }
    }
}
