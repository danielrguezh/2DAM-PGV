package org.docencia.hilos.ejercicio3;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

/**
 * Clase que  spawnea enemigos en mundo abierto
 */
public class SpawnsMundoAbierto {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final AtomicInteger contadorSpawns = new AtomicInteger(0);
    private static final Random random = new Random();

    /**
     * Constantes de configuración del sistema de spawns
     */
    public static class ConfigSpawns {
        public static final int POOL_SIZE_SCHEDULER = 2;
        public static final int PERIODO_SPAWN_NORMAL_SEG = 2;
        public static final int DURACION_MUNDO_SEG = 12;
        public static final int TIEMPO_ESPERA_SHUTDOWN_SEG = 5;
        public static final int DELAY_SPAWN_BOSS_SEG = 3;
        public static final long TIEMPO_SPAWN_LENTO_MS = 3000;
        public static final int PERIODO_SPAWN_LENTO_SEG = 2;
    }

    /**
     * Tarea de spawn con configuracion de tiempo de ejecucion
     */
    static class SpawnTarea implements Runnable {

        private final long tiempoEjecucion;
        private final boolean usarProbabilidades;

        public SpawnTarea(long tiempoEjecucion, boolean usarProbabilidades) {
            this.tiempoEjecucion = tiempoEjecucion;
            this.usarProbabilidades = usarProbabilidades;
        }

        @Override
        public void run() {
            long inicio = System.currentTimeMillis();
            String hilo = Thread.currentThread().getName();
            
            Zona zona = Zona.aleatoria();
            TipoEnemigo enemigo = usarProbabilidades 
                    ? TipoEnemigo.aleatorioConProbabilidad()
                    : TipoEnemigo.values()[random.nextInt(TipoEnemigo.values().length)];
            
            int numSpawn = contadorSpawns.incrementAndGet();
            
            System.out.println(
                String.format("[%s][%s] Spawn #%d: %s en %s",
                    LocalTime.now().format(FORMATO_HORA),
                    hilo,
                    numSpawn,
                    enemigo,
                    zona
                ));

            if (tiempoEjecucion > 0) {
                try {
                    Thread.sleep(tiempoEjecucion);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            long fin = System.currentTimeMillis();
            long duracion = fin - inicio;
            if (duracion > 100) {
                System.out.println("Spawn tardo " + duracion + "ms");
            }
        }
    }

    /**
     * Tarea especifica para spawn de boss
     */
    static class SpawnBossTarea implements Runnable {
        private final TipoEnemigo boss;
        private final Zona zona;

        public SpawnBossTarea(TipoEnemigo boss, Zona zona) {
            this.boss = boss;
            this.zona = zona;
        }

        @Override
        public void run() {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("¡ALERTA DE BOSS!");
            System.out.println(String.format("   %s ha aparecido en %s", boss, zona));
            System.out.println("   ¡Todos los jugadores, preparense para el combate!");
            System.out.println("=".repeat(60) + "\n");
        }
    }

    /**
     * Ejecuta spawns normales cada 2 segundos
     */
    public static void ejecutarSpawnsNormales() throws InterruptedException {
        System.out.println("=== SPAWNS NORMALES (cada " + ConfigSpawns.PERIODO_SPAWN_NORMAL_SEG + " segundos) ===\n");
        
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(ConfigSpawns.POOL_SIZE_SCHEDULER);
        contadorSpawns.set(0);

        scheduler.scheduleAtFixedRate(
                new SpawnTarea(0, true),
                0, 
                ConfigSpawns.PERIODO_SPAWN_NORMAL_SEG, 
                TimeUnit.SECONDS
        );

        Thread.sleep(ConfigSpawns.DURACION_MUNDO_SEG * 1000L);

        System.out.println("\nDeteniendo spawns normales...");
        scheduler.shutdown();
        if (!scheduler.awaitTermination(ConfigSpawns.TIEMPO_ESPERA_SHUTDOWN_SEG, TimeUnit.SECONDS)) {
            System.out.println("Forzando parada de spawns.");
            scheduler.shutdownNow();
        }
        System.out.println("Total de spawns generados: " + contadorSpawns.get());
        mostrarEstadisticas();
    }

    /**
     * Ejecuta un spawn unico retrasado (schedule)
     */
    public static void ejecutarSpawnUnico() throws InterruptedException {
        System.out.println("\n=== SPAWN UNICO RETRASADO (después de " + ConfigSpawns.DELAY_SPAWN_BOSS_SEG + " segundos) ===\n");
        
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        contadorSpawns.set(0);

        System.out.println("Programando spawn de boss...");
        
        scheduler.schedule(
                new SpawnBossTarea(TipoEnemigo.DRAGON_ANCESTRAL, Zona.VOLCAN_OSCURO),
                ConfigSpawns.DELAY_SPAWN_BOSS_SEG, 
                TimeUnit.SECONDS
        );

        Thread.sleep(5000);
        
        scheduler.shutdown();
        scheduler.awaitTermination(2, TimeUnit.SECONDS);
    }

    /**
     * Demuestra que pasa cuando la tarea tarda mas que el periodo
     */
    public static void ejecutarSpawnsLentos() throws InterruptedException {
        System.out.println("\n=== SPAWNS LENTOS (tarea tarda 3s, periodo 2s) ===\n");
        System.out.println("Observa como se acumulan los spawns si tardan mas que el periodo\n");
        
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(ConfigSpawns.POOL_SIZE_SCHEDULER);
        contadorSpawns.set(0);

        scheduler.scheduleAtFixedRate(
                new SpawnTarea(ConfigSpawns.TIEMPO_SPAWN_LENTO_MS, false),
                0, 
                ConfigSpawns.PERIODO_SPAWN_LENTO_SEG, 
                TimeUnit.SECONDS
        );

        Thread.sleep(12000);

        System.out.println("\nDeteniendo spawns lentos...");
        scheduler.shutdown();
        if (!scheduler.awaitTermination(8, TimeUnit.SECONDS)) {
            scheduler.shutdownNow();
        }
        System.out.println("Total de spawns iniciados: " + contadorSpawns.get());
        System.out.println("Nota: Las tareas NO se solapan, esperan a que termine la anterior");
    }

    /**
     * Prueba con diferentes periodos
     */
    public static void probarDiferentesPeriodos() throws InterruptedException {
        System.out.println("\n=== COMPARACION DE PERIODOS ===\n");

        int[] periodos = {1, 3, 5};

        for (int periodo : periodos) {
            System.out.println("--- Spawns cada " + periodo + " segundo(s) ---");
            
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            contadorSpawns.set(0);

            scheduler.scheduleAtFixedRate(
                    new SpawnTarea(0, false), 
                    0, 
                    periodo, 
                    TimeUnit.SECONDS
            );

            Thread.sleep(6000);

            scheduler.shutdown();
            scheduler.awaitTermination(2, TimeUnit.SECONDS);

            System.out.println("   Spawns en 6 segundos: " + contadorSpawns.get() + "\n");
        }
    }

    /**
     * Muestra estadIsticas de rareza
     */
    private static void mostrarEstadisticas() {
        System.out.println("\nPROBABILIDADES DE RAREZA:");
        for (Rareza rareza : Rareza.values()) {
            System.out.println(String.format("   %s: %.1f%%%n",
                    rareza.getNombre(),
                    rareza.getProbabilidad()));
        }
    }

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║  SPAWNS DE ENEMIGOS EN MUNDO ABIERTO       ║");
        System.out.println("║  Ejercicio 3: ScheduledExecutorService     ║");
        System.out.println("║  MEJORADO: Con Enums y Constantes          ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        try {
            ejecutarSpawnsNormales();
            Thread.sleep(2000);
            ejecutarSpawnUnico();
            Thread.sleep(2000);
            ejecutarSpawnsLentos();
            Thread.sleep(2000);
            probarDiferentesPeriodos();
        } catch (InterruptedException e) {
            System.out.println("Sistema de spawns interrumpido");
            Thread.currentThread().interrupt();
        }
    }
}
