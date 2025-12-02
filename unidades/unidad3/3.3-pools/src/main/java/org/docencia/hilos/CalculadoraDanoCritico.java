package org.docencia.hilos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author danielrguezh
 * @version 1.0.0
 */
public class CalculadoraDanoCritico {

    /**
     * Clase que simula el danio de un ataque
     */
    static class Ataque {
        final String atacante;
        final int danoBase;
        final double probCritico;
        final double multiplicadorCritico;

        /**
         * Constructor con todas las propiedades
         * @param atacante del ataque
         * @param danoBase del ataque
         * @param probCritico del ataque
         * @param multiplicadorCritico del ataque
         */
        Ataque(String atacante, int danoBase, double probCritico, double multiplicadorCritico) {
            this.atacante = atacante;
            this.danoBase = danoBase;
            this.probCritico = probCritico;
            this.multiplicadorCritico = multiplicadorCritico;
        }
    }

    /**
     * Clase que calcula el danio del ataque con Callable
     */
    static class TareaCalcularDano implements Callable<Integer> {
        private final Ataque ataque;

        /**
         * Constructor con la propiedad ataque
         * @param ataque a calcular el danio
         */
        TareaCalcularDano(Ataque ataque) {
            this.ataque = ataque;
        }

        @Override
        public Integer call() throws Exception {
            String hilo = Thread.currentThread().getName();
            System.out.println("[" + hilo + "] Calculando danio para " + ataque.atacante);

            boolean esCritico = Math.random() < ataque.probCritico;
            double multiplicador = esCritico ? ataque.multiplicadorCritico : 1.0;

            Thread.sleep(500 + (int)(Math.random() * 500));

            int danoFinal = (int) (ataque.danoBase * multiplicador);
            System.out.println("[" + hilo + "] " + ataque.atacante +
                    (esCritico ? " ¡CRITICO!" : " golpe normal") +
                    " -> danio: " + danoFinal);

            return danoFinal;
        }
    }

    /**
     * Clase que calcula el danio del ataque con Runnable
     */
    static class TareaCalcularDanoRunnable implements Runnable {
        private final Ataque ataque;

        /**
         * Constructor con la propiedad ataque
         * @param ataque a calcular el danio
         */
        TareaCalcularDanoRunnable(Ataque ataque) {
            this.ataque = ataque;
        }

        @Override
        public void run() {
            String hilo = Thread.currentThread().getName();
            System.out.println("[" + hilo + "] [RUNNABLE] Calculando danio para " + ataque.atacante);

            boolean esCritico = Math.random() < ataque.probCritico;
            double multiplicador = esCritico ? ataque.multiplicadorCritico : 1.0;

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            int danoFinal = (int) (ataque.danoBase * multiplicador);
            System.out.println("[" + hilo + "] [RUNNABLE] " + ataque.atacante +
                    " -> danio: " + danoFinal + " (NO SE PUEDE RECOGER EL VALOR)");
        }
    }

    private static void demostrarCallable() {
        System.out.println("=== PARTE 1: Usando Callable<Integer> (CON valor de retorno) ===\n");
        
        ExecutorService pool = Executors.newFixedThreadPool(4);
        List<Future<Integer>> futuros = new ArrayList<>();

        Ataque[] ataques = {
                new Ataque("Mago del Fuego", 120, 0.30, 2.5),
                new Ataque("Guerrero", 150, 0.15, 2.0),
                new Ataque("Pícaro", 90, 0.50, 3.0),
                new Ataque("Arquera Élfica", 110, 0.35, 2.2),
                new Ataque("Invocador", 80, 0.40, 2.8),
                new Ataque("Paladín", 130, 0.10, 1.8),
                new Ataque("Bárbaro", 160, 0.20, 2.1),
                new Ataque("Nigromante", 100, 0.25, 2.3),
        };

        for (Ataque ataque : ataques) {
            Future<Integer> futuro = pool.submit(new TareaCalcularDano(ataque));
            futuros.add(futuro);
        }

        int totalRaid = 0;
        int ataquesProcesados = 0;
        for (int i = 0; i < ataques.length; i++) {
            try {
                int dano = futuros.get(i).get();
                totalRaid += dano;
                ataquesProcesados++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Lectura de resultado interrumpida");
            } catch (ExecutionException e) {
                System.out.println("Error calculando daño: " + e.getCause());
            }
        }

        System.out.println("\n RESULTADOS RAID:");
        System.out.println("   Ataques procesados: " + ataquesProcesados);
        System.out.println("   Daño total: " + totalRaid);
        System.out.println("   Daño promedio: " + (totalRaid / ataquesProcesados));
        
        pool.shutdown();
    }

    private static void demostrarRunnable() {
        System.out.println("=== PARTE 2: Usando Runnable (SIN valor de retorno) ===\n");
        
        ExecutorService pool = Executors.newFixedThreadPool(3);

        Ataque[] ataques = {
                new Ataque("Caballero", 100, 0.20, 2.0),
                new Ataque("Druida", 85, 0.30, 2.3),
                new Ataque("Monje", 95, 0.25, 2.1),
        };

        for (Ataque ataque : ataques) {
            pool.execute(new TareaCalcularDanoRunnable(ataque));
        }

        pool.shutdown();
        try {
            pool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n PROBLEMA: Con Runnable NO podemos sumar el danio total");
        System.out.println("   porque execute() no devuelve Future<V>");
    }

    private static void compararProbabilidadesCritico() {
        System.out.println("=== PARTE 3: Comparación con diferentes probabilidades ===\n");

        double[] probabilidades = {0.10, 0.30, 0.50, 0.80};

        for (double prob : probabilidades) {
            System.out.println("--- Probabilidad de crítico: " + (int)(prob * 100) + "% ---");
            
            ExecutorService pool = Executors.newFixedThreadPool(3);
            List<Future<Integer>> futuros = new ArrayList<>();

            for (int i = 1; i <= 5; i++) {
                Ataque ataque = new Ataque("Atacante " + i, 100, prob, 2.0);
                futuros.add(pool.submit(new TareaCalcularDano(ataque)));
            }

            int total = 0;
            for (Future<Integer> f : futuros) {
                try {
                    total += f.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("   Danio total con probabilidad " + (int)(prob * 100) + "%: " + total + "\n");
            
            pool.shutdown();
            try {
                pool.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║  CALCULADORA DE DANIO CRITICO              ║");
        System.out.println("║  Ejercicio 2: Callable + Future            ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        demostrarCallable();

        System.out.println("\n" + "=".repeat(60) + "\n");
        demostrarRunnable();

        System.out.println("\n" + "=".repeat(60) + "\n");
        compararProbabilidadesCritico();
    }
}
