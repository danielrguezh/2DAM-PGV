package org.docencia.hilos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ServidorMazmorrasTest {

    @Test
    @DisplayName("Test basico: Las peticiones se ejecutan correctamente")
    @Timeout(10)
    void testPeticionesSeEjecutan() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        AtomicInteger completadas = new AtomicInteger(0);

        for (int i = 0; i < 5; i++) {
            pool.execute(() -> {
                try {
                    Thread.sleep(100);
                    completadas.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        pool.shutdown();
        assertTrue(pool.awaitTermination(5, TimeUnit.SECONDS));
        assertEquals(5, completadas.get(), "Todas las peticiones deben completarse");
    }

    @Test
    @DisplayName("Test: Pool de 1 hilo procesa tareas secuencialmente")
    @Timeout(15)
    void testPoolUnHilo() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        ConcurrentLinkedQueue<String> orden = new ConcurrentLinkedQueue<>();

        for (int i = 1; i <= 3; i++) {
            final int num = i;
            pool.execute(() -> {
                orden.add("Tarea-" + num);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(3, orden.size());
        assertTrue(orden.contains("Tarea-1"));
    }

    @Test
    @DisplayName("Test: Pool de 3 hilos permite concurrencia")
    @Timeout(5)
    void testPoolTresHilos() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);
        AtomicInteger concurrentes = new AtomicInteger(0);
        AtomicInteger maxConcurrentes = new AtomicInteger(0);

        for (int i = 0; i < 3; i++) {
            pool.execute(() -> {
                int actual = concurrentes.incrementAndGet();
                maxConcurrentes.updateAndGet(max -> Math.max(max, actual));
                
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    concurrentes.decrementAndGet();
                    latch.countDown();
                }
            });
        }

        latch.await();
        pool.shutdown();

        assertTrue(maxConcurrentes.get() >= 2, 
        "Con pool de 3 hilos, debe haber al menos 2 tareas concurrentes");
    }

    @Test
    @DisplayName("Test: Shutdown ordena pero no fuerza cierre inmediato")
    void testShutdown() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        
        pool.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        pool.shutdown();
        assertTrue(pool.isShutdown(), "Pool debe estar en estado shutdown");
        assertFalse(pool.isTerminated(), "Pool aun tiene tareas ejecutandose");
    }

    @Test
    @DisplayName("Test: PeticionMazmorra contiene datos correctos")
    void testPeticionMazmorra() {
        ServidorMazmorras.PeticionMazmorra peticion = 
                new ServidorMazmorras.PeticionMazmorra("Link", "Catacumbas");

        assertEquals("Link", peticion.getNombreJugador());
        assertEquals("Catacumbas", peticion.getMazmorra());
    }

    @Test
    @DisplayName("Test: Manejo de interrupciones")
    @Timeout(5)
    void testInterrupcion() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger interrupciones = new AtomicInteger(0);

        Future<?> future = pool.submit(() -> {
            try {
                latch.countDown();
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                interrupciones.incrementAndGet();
                Thread.currentThread().interrupt();
            }
        });

        latch.await();
        future.cancel(true);

        pool.shutdown();
        pool.awaitTermination(2, TimeUnit.SECONDS);

        assertEquals(1, interrupciones.get(), "Debe detectar la interrupcion");
    }
}
