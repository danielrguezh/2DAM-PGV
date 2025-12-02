package org.docencia.hilos.ejercicio3;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpawnsMundoAbiertoTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ScheduledExecutorService scheduler;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
        scheduler = Executors.newScheduledThreadPool(2);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        System.setOut(originalOut);
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            scheduler.awaitTermination(5, TimeUnit.SECONDS);
        }
        outputStream.reset();
    }

    @Test
    @DisplayName("Test: Verificar constantes de configuracion")
    void testConfiguracionConstantes() {
        assertEquals(2, SpawnsMundoAbierto.ConfigSpawns.POOL_SIZE_SCHEDULER);
        assertEquals(2, SpawnsMundoAbierto.ConfigSpawns.PERIODO_SPAWN_NORMAL_SEG);
        assertEquals(12, SpawnsMundoAbierto.ConfigSpawns.DURACION_MUNDO_SEG);
        assertEquals(5, SpawnsMundoAbierto.ConfigSpawns.TIEMPO_ESPERA_SHUTDOWN_SEG);
        assertEquals(3, SpawnsMundoAbierto.ConfigSpawns.DELAY_SPAWN_BOSS_SEG);
        assertEquals(3000L, SpawnsMundoAbierto.ConfigSpawns.TIEMPO_SPAWN_LENTO_MS);
        assertEquals(2, SpawnsMundoAbierto.ConfigSpawns.PERIODO_SPAWN_LENTO_SEG);
    }

    @Test
    @DisplayName("Test: SpawnTarea se ejecuta correctamente")
    void testSpawnTareaEjecucion() throws InterruptedException {
        AtomicInteger contador = new AtomicInteger(0);
        
        SpawnsMundoAbierto.SpawnTarea tarea = new SpawnsMundoAbierto.SpawnTarea(0, false);
        
        CountDownLatch latch = new CountDownLatch(1);
        
        new Thread(() -> {
            tarea.run();
            contador.incrementAndGet();
            latch.countDown();
        }).start();
        
        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertEquals(1, contador.get());
        
        String output = outputStream.toString();
        assertTrue(output.contains("Spawn #"));
    }

    @Test
    @DisplayName("Test: SpawnTarea con tiempo de ejecucion")
    void testSpawnTareaConDelay() {
        long tiempoEjecucion = 500;
        SpawnsMundoAbierto.SpawnTarea tarea = new SpawnsMundoAbierto.SpawnTarea(tiempoEjecucion, false);
        
        long inicio = System.currentTimeMillis();
        tarea.run();
        long fin = System.currentTimeMillis();
        
        long duracion = fin - inicio;
        assertTrue(duracion >= tiempoEjecucion, 
            "La tarea debería tardar al menos " + tiempoEjecucion + "ms, tardó " + duracion + "ms");
    }

    @Test
    @DisplayName("Test: SpawnTarea maneja interrupciones")
    void testSpawnTareaInterrupcion() throws InterruptedException {
        SpawnsMundoAbierto.SpawnTarea tarea = new SpawnsMundoAbierto.SpawnTarea(5000, false);
        
        Thread thread = new Thread(tarea);
        thread.start();
        
        Thread.sleep(100);
        thread.interrupt();
        thread.join(1000);
        
        assertTrue(thread.isInterrupted() || !thread.isAlive());
    }

    @Test
    @DisplayName("Test: SpawnBossTarea genera salida correcta")
    void testSpawnBossTarea() {
        SpawnsMundoAbierto.SpawnBossTarea bossTarea = 
            new SpawnsMundoAbierto.SpawnBossTarea(TipoEnemigo.DRAGON_ANCESTRAL, Zona.VOLCAN_OSCURO);
        
        bossTarea.run();
        
        String output = outputStream.toString();
        assertTrue(output.contains("ALERTA DE BOSS"));
        assertTrue(output.contains("VOLCAN_OSCURO"));
        assertTrue(output.contains("preparense para el combate"));
    }

    @Test
    @DisplayName("Test: ScheduleAtFixedRate ejecuta multiples veces")
    void testScheduleAtFixedRate() throws InterruptedException {
        AtomicInteger contador = new AtomicInteger(0);
        
        scheduler.scheduleAtFixedRate(
            () -> contador.incrementAndGet(),
            0,
            200,
            TimeUnit.MILLISECONDS
        );
        
        Thread.sleep(1000);
        
        int ejecuciones = contador.get();
        assertTrue(ejecuciones >= 4 && ejecuciones <= 6, 
            "Se esperaban entre 4 y 6 ejecuciones, se obtuvieron " + ejecuciones);
    }

    @Test
    @DisplayName("Test: Schedule ejecuta una sola vez con delay")
    void testScheduleConDelay() throws InterruptedException {
        AtomicInteger contador = new AtomicInteger(0);
        long delay = 500;
        
        long inicio = System.currentTimeMillis();
        
        scheduler.schedule(
            () -> contador.incrementAndGet(),
            delay,
            TimeUnit.MILLISECONDS
        );
        
        Thread.sleep(300);
        assertEquals(0, contador.get(), "No deberia haberse ejecutado aun");
        
        Thread.sleep(300);
        long fin = System.currentTimeMillis();
        
        assertEquals(1, contador.get(), "Deberia haberse ejecutado exactamente una vez");
        assertTrue(fin - inicio >= delay, "Deberia haber esperado al menos el delay");
    }

    @Test
    @DisplayName("Test: Shutdown detiene nuevas tareas")
    void testSchedulerShutdown() throws InterruptedException {
        AtomicInteger contador = new AtomicInteger(0);
        
        scheduler.scheduleAtFixedRate(
            () -> contador.incrementAndGet(),
            0,
            100,
            TimeUnit.MILLISECONDS
        );
        
        Thread.sleep(300);
        int contadorAntes = contador.get();
        
        scheduler.shutdown();
        assertTrue(scheduler.isShutdown());
        
        Thread.sleep(300);
        int contadorDespues = contador.get();
        
        assertTrue(contadorDespues <= contadorAntes + 3);
    }

    @Test
    @DisplayName("Test: ShutdownNow interrumpe tareas en ejecucion")
    void testSchedulerShutdownNow() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        scheduler.scheduleAtFixedRate(
            () -> {
                latch.countDown();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            },
            0,
            100,
            TimeUnit.MILLISECONDS
        );
        
        assertTrue(latch.await(1, TimeUnit.SECONDS));
        
        scheduler.shutdownNow();
        assertTrue(scheduler.isShutdown());
    }

    @Test
    @DisplayName("Test: Multiples spawns concurrentes")
    void testSpawnsConcurrentes() throws InterruptedException {
        AtomicInteger contador = new AtomicInteger(0);
        int numTareas = 10;
        CountDownLatch latch = new CountDownLatch(numTareas);
        
        for (int i = 0; i < numTareas; i++) {
            scheduler.schedule(
                () -> {
                    contador.incrementAndGet();
                    latch.countDown();
                },
                0,
                TimeUnit.MILLISECONDS
            );
        }
        
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(numTareas, contador.get());
    }

    @Test
    @DisplayName("Test: Pool size limita ejecuciones simultaneas")
    void testPoolSizeLimite() throws InterruptedException {
        ScheduledExecutorService limitedScheduler = Executors.newScheduledThreadPool(1);
        
        try {
            AtomicInteger ejecutandose = new AtomicInteger(0);
            AtomicInteger maxSimultaneos = new AtomicInteger(0);
            CountDownLatch latch = new CountDownLatch(5);
            
            for (int i = 0; i < 5; i++) {
                limitedScheduler.schedule(
                    () -> {
                        int actual = ejecutandose.incrementAndGet();
                        maxSimultaneos.updateAndGet(max -> Math.max(max, actual));
                        
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        
                        ejecutandose.decrementAndGet();
                        latch.countDown();
                    },
                    0,
                    TimeUnit.MILLISECONDS
                );
            }
            
            assertTrue(latch.await(5, TimeUnit.SECONDS));
            assertEquals(1, maxSimultaneos.get(), 
                "Con pool size 1, solo deberia ejecutarse 1 tarea a la vez");
        } finally {
            limitedScheduler.shutdown();
            limitedScheduler.awaitTermination(2, TimeUnit.SECONDS);
        }
    }

    @Test
    @DisplayName("Test: Verificar que tareas lentas no se solapan")
    void testTareasLentasNoSeSolapan() throws InterruptedException {
        AtomicInteger ejecutandose = new AtomicInteger(0);
        AtomicInteger maxSimultaneos = new AtomicInteger(0);
        
        scheduler.scheduleAtFixedRate(
            () -> {
                int actual = ejecutandose.incrementAndGet();
                maxSimultaneos.updateAndGet(max -> Math.max(max, actual));
                
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                ejecutandose.decrementAndGet();
            },
            0,
            100,
            TimeUnit.MILLISECONDS
        );
        
        Thread.sleep(1000);
        
        assertTrue(maxSimultaneos.get() <= 2, 
            "Con pool size 2, maximo 2 tareas simultaneas. Encontradas: " + maxSimultaneos.get());
    }

    @Test
    @DisplayName("Test: Flujo completo de spawn normal")
    void testFlujoCompletoSpawnNormal() throws InterruptedException {
        AtomicInteger spawns = new AtomicInteger(0);
        
        scheduler.scheduleAtFixedRate(
            () -> spawns.incrementAndGet(),
            0,
            1,
            TimeUnit.SECONDS
        );
        
        Thread.sleep(3500);
        
        scheduler.shutdown();
        assertTrue(scheduler.awaitTermination(2, TimeUnit.SECONDS));
        
        int totalSpawns = spawns.get();
        assertTrue(totalSpawns >= 3 && totalSpawns <= 5, 
            "En 3.5 segundos con periodo de 1s, esperamos 3-5 spawns. Obtenidos: " + totalSpawns);
    }

    @Test
    @DisplayName("Test: Timeout en awaitTermination")
    void testAwaitTerminationTimeout() throws InterruptedException {
        scheduler.scheduleAtFixedRate(
            () -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            },
            0,
            100,
            TimeUnit.MILLISECONDS
        );
        
        Thread.sleep(200);
        scheduler.shutdown();
        
        long inicio = System.currentTimeMillis();
        boolean terminado = scheduler.awaitTermination(500, TimeUnit.MILLISECONDS);
        long duracion = System.currentTimeMillis() - inicio;
        
        assertFalse(terminado, "No deberia haber terminado en 500ms");
        assertTrue(duracion >= 500 && duracion < 1000, 
            "Deberia haber esperado ~500ms, espero " + duracion + "ms");
        
        scheduler.shutdownNow();
    }
}