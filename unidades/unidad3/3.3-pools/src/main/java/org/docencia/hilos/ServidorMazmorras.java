package org.docencia.hilos;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

/**
 * Clase que simula un servidor de mazmorras
 */
public class ServidorMazmorras {

    /**
     * Clase que se encarga de las peticiones a la mazmorras
     */
    static class PeticionMazmorra implements Runnable {
        private final String nombreJugador;
        private final String mazmorra;

        /**
         * Constructor con todas las propiedades
         * @param nombreJugador nombre del jugador
         * @param mazmorra nombre de la mazmorra
         */
        public PeticionMazmorra(String nombreJugador, String mazmorra) {
            this.nombreJugador = nombreJugador;
            this.mazmorra = mazmorra;
        }

        /**
         * Getters
         */
        public String getNombreJugador() {
            return nombreJugador;
        }

        public String getMazmorra() {
            return mazmorra;
        }

        @Override
        public void run() {
            String hilo = Thread.currentThread().getName();
            System.out.println("[" + hilo + "] Preparando mazmorra '" + mazmorra +
                    "' para el jugador " + nombreJugador);
            try {
                Thread.sleep(1000 + (int)(Math.random() * 1000));
            } catch (InterruptedException e) {
                System.out.println("[" + hilo + "] Petición de " + nombreJugador + " interrumpida");
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("[" + hilo + "] Mazmorra '" + mazmorra +
                    "' lista para " + nombreJugador + " 🎮");
        }
    }

    /**
     * Metodo que ejecuta el servidor
     * @param tamanoPool tamanio del ThreadPool 
     */
    public static void ejecutarServidor(int tamanoPool) {
        System.out.println("\n=== SERVIDOR DE MAZMORRAS (Pool de " + tamanoPool + " hilos) ===\n");
        
        ExecutorService gmBots = Executors.newFixedThreadPool(tamanoPool);

        String[] jugadores = {
                "Link", "Zelda", "Geralt", "Yennefer", "Gandalf",
                "Frodo", "Aragorn", "Leia", "Luke", "DarthVader"
        };
        String[] mazmorras = {
                "Catacumbas de Hyrule", "Torre Oscura", "Moria",
                "Estrella de la Muerte", "Nido de Dragón"
        };

        for (int i = 0; i < jugadores.length; i++) {
            String jugador = jugadores[i];
            String dungeon = mazmorras[i % mazmorras.length];
            gmBots.execute(new PeticionMazmorra(jugador, dungeon));
        }

        gmBots.shutdown();
        System.out.println("Servidor: todas las peticiones han sido enviadas a los GM bots.\n");
        
        try {
            if (!gmBots.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("Tiempo agotado, forzando cierre...");
                gmBots.shutdownNow();
            }
        } catch (InterruptedException e) {
            gmBots.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("=== Servidor finalizado ===\n");
    }

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║  SERVIDOR DE MAZMORRAS ONLINE              ║");
        System.out.println("║  Ejercicio 1: Fixed Thread Pool            ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        ejecutarServidor(3);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("COMPARACIÓN: Pool de 1 hilo (procesamiento secuencial)");
        System.out.println("=".repeat(50));
        ejecutarServidor(1);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("COMPARACIÓN: Pool de 10 hilos (alta concurrencia)");
        System.out.println("=".repeat(50));
        ejecutarServidor(10);
    }
}
