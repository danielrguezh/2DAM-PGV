package org.docencia.hilos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraDanoCriticoTest {

    @Test
    @DisplayName("Test: Callable devuelve resultado correctamente")
    @Timeout(5)
    void testCallableDevuelveResultado() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        
        CalculadoraDanoCritico.Ataque ataque = 
                new CalculadoraDanoCritico.Ataque("Guerrero", 100, 0.0, 1.0);
        
        CalculadoraDanoCritico.TareaCalcularDano tarea = 
                new CalculadoraDanoCritico.TareaCalcularDano(ataque);
        
        Future<Integer> future = pool.submit(tarea);
        Integer resultado = future.get();

        assertNotNull(resultado);
        assertEquals(100, resultado, "Sin crítico, daño debe ser igual al base");

        pool.shutdown();
    }

    @Test
    @DisplayName("Test: Multiples Futures se recogen correctamente")
    @Timeout(10)
    void testMultiplesFutures() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        List<Future<Integer>> futures = new ArrayList<>();

        // Creamos 5 ataques sin crítico
        for (int i = 0; i < 5; i++) {
            CalculadoraDanoCritico.Ataque ataque = 
                    new CalculadoraDanoCritico.Ataque("Atacante" + i, 100, 0.0, 1.0);
            futures.add(pool.submit(new CalculadoraDanoCritico.TareaCalcularDano(ataque)));
        }

        int total = 0;
        for (Future<Integer> future : futures) {
            total += future.get();
        }

        assertEquals(500, total, "5 ataques de 100 = 500 de daño total");
        pool.shutdown();
    }

    @Test
    @DisplayName("Test: Critico aumenta el danio")
    @Timeout(5)
    void testCriticoAumentaDano() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(1);

        CalculadoraDanoCritico.Ataque ataque = 
                new CalculadoraDanoCritico.Ataque("Asesino", 100, 1.0, 2.0);

        Future<Integer> future = pool.submit(
                new CalculadoraDanoCritico.TareaCalcularDano(ataque));
        
        Integer dano = future.get();

        assertEquals(200, dano, "Con 100% crítico y x2, debe hacer 200 de daño");
        pool.shutdown();
    }

    @Test
    @DisplayName("Test: Future.isDone() funciona correctamente")
    @Timeout(5)
    void testFutureIsDone() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(1);

        CalculadoraDanoCritico.Ataque ataque = 
                new CalculadoraDanoCritico.Ataque("Mago", 80, 0.5, 2.0);

        Future<Integer> future = pool.submit(
                new CalculadoraDanoCritico.TareaCalcularDano(ataque));

        assertFalse(future.isDone(), "No debe estar completo inmediatamente");

        future.get();

        assertTrue(future.isDone(), "Debe estar completo después de get()");
        pool.shutdown();
    }

    @Test
    @DisplayName("Test: Future puede cancelarse")
    @Timeout(5)
    void testFutureCancelacion() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);

        Future<Integer> future = pool.submit(() -> {
            Thread.sleep(10000);
            return 100;
        });

        Thread.sleep(100);
        boolean cancelado = future.cancel(true);

        assertTrue(cancelado, "Debe poder cancelarse");
        assertTrue(future.isCancelled(), "Debe estar en estado cancelado");

        pool.shutdown();
    }

    @Test
    @DisplayName("Test: ExecutionException captura excepciones de Callable")
    @Timeout(5)
    void testExecutionException() {
        ExecutorService pool = Executors.newFixedThreadPool(1);

        Future<Integer> future = pool.submit(() -> {
            throw new RuntimeException("Error simulado");
        });

        ExecutionException exception = assertThrows(ExecutionException.class, 
                future::get,
                "Debe lanzar ExecutionException");

        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Error simulado", exception.getCause().getMessage());

        pool.shutdown();
    }

    @Test
    @DisplayName("Test: Comparación de tiempos con diferentes tamaños de pool")
    @Timeout(15)
    void testComparacionTiemposPool() throws Exception {
        long tiempoPoolPequeno = medirTiempoEjecucion(2, 8);
        long tiempoPoolGrande = medirTiempoEjecucion(8, 8);

        System.out.println("Tiempo con pool de 2: " + tiempoPoolPequeno + "ms");
        System.out.println("Tiempo con pool de 8: " + tiempoPoolGrande + "ms");

        assertTrue(tiempoPoolGrande <= tiempoPoolPequeno,
                "Pool más grande debe ser igual o más rápido");
    }

    private long medirTiempoEjecucion(int tamanoPool, int numTareas) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(tamanoPool);
        List<Future<Integer>> futures = new ArrayList<>();

        long inicio = System.currentTimeMillis();

        for (int i = 0; i < numTareas; i++) {
            futures.add(pool.submit(() -> {
                Thread.sleep(200);
                return 100;
            }));
        }

        for (Future<Integer> f : futures) {
            f.get();
        }

        long fin = System.currentTimeMillis();
        pool.shutdown();

        return fin - inicio;
    }
}
