# Code, Learn & Practice(Programación de Servicios y Procesos: "Cazadores de Monstruos")

En esta tarea vamos a aprender **thread pools en Java** simulando un **servidor de videojuegos** con varios escenarios:

1. 🏰 **Servidor de Mazmorras Online** → `ExecutorService` + `Runnable`  
2. ⚔️ **Calculadora de Daño Crítico** → `Callable` + `Future`  
3. 👹 **Spawns de Enemigos en un Mundo Abierto** → `ScheduledExecutorService`  

La idea es que veas **para qué sirven los pools de hilos** y cómo usarlos de forma correcta, pero disfrazado de cosas que te suenen a MMORPG, D&D o similar.

> __IMPORTANTE__: Debes de crear el proyecto en java, con las dependencias necesarias, y realizar los test necesarios para verificar el correcto funcionamiento.

---

## 1. Objetivos de la tarea

Al terminar deberías ser capaz de:

- Entender qué es un **thread pool** y por qué es mejor que crear hilos a lo loco.
- Usar `ExecutorService` con distintos tipos de pool:
  - `newFixedThreadPool(...)`
  - `newScheduledThreadPool(...)`
- Enviar tareas:
  - `Runnable` (no devuelven valor, solo hacen cosas).
  - `Callable<V>` (devuelven un resultado).
- Usar `Future<V>` para recuperar resultados de tareas.
- Cerrar correctamente un pool:
  - `shutdown()`, `awaitTermination(...)`, `shutdownNow()`.

---


Estructura sugerida:

```text
src/main/org/docencia/hilos/
├── ServidorMazmorras.java
├── CalculadoraDanoCritico.java
└── SpawnsMundoAbierto.java
```

---

## 2. Ejercicio 1 – 🏰 Servidor de Mazmorras Online (Fixed Thread Pool + Runnable)

### 2.1. Historia

Tienes un **servidor de mazmorras online**. Cada vez que un jugador entra a una mazmorra, el servidor debe:

- Validar al jugador.
- Preparar la instancia.
- Cargar enemigos, loot, etc.

En vez de crear un hilo por jugador, tienes un **equipo de 3 “GM bots”** (hilos del pool) que se encargan de procesar todas las peticiones de forma ordenada.

### 2.2. Qué vas a implementar

- Una tarea `Runnable` que representa una **petición de entrada a mazmorra**.
- Un pool de hilos fijo (`newFixedThreadPool(3)`) que actuará como los GM bots.
- Un `main` que simula varios jugadores intentando entrar a mazmorras distintas.

### 2.3. Código base 

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorMazmorras {

    static class PeticionMazmorra implements Runnable {
        private final String nombreJugador;
        private final String mazmorra;

        public PeticionMazmorra(String nombreJugador, String mazmorra) {
            this.nombreJugador = nombreJugador;
            this.mazmorra = mazmorra;
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

    public static void main(String[] args) {

        ExecutorService gmBots = Executors.newFixedThreadPool(3); // Creamos un pool de 3

        // Simulamos 10 jugadores que quieren entrar a mazmorras
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
        System.out.println("Servidor: todas las peticiones han sido enviadas a los GM bots.");
    }
}
```

### 2.3. Responde y comenta la salida ejecutando los cambios que se proponen.

#### Solo se usan **3 hilos** (3 GM bots) para atender a todos los jugadores, ¿que esta sucediendo?.

**Respuesta:**

Cuando ejecutamos el codigo con un pool de 3 hilos, observamos que:

```
=== SERVIDOR DE MAZMORRAS (Pool de 3 hilos) ===

Servidor: todas las peticiones han sido enviadas a los GM bots.

[pool-1-thread-1] Preparando mazmorra 'Catacumbas de Hyrule' para el jugador Link
[pool-1-thread-2] Preparando mazmorra 'Torre Oscura' para el jugador Zelda
[pool-1-thread-3] Preparando mazmorra 'Moria' para el jugador Geralt
[pool-1-thread-1] Mazmorra 'Catacumbas de Hyrule' lista para Link 🎮
[pool-1-thread-1] Preparando mazmorra 'Estrella de la Muerte' para el jugador Yennefer
[pool-1-thread-2] Mazmorra 'Torre Oscura' lista para Zelda 🎮
[pool-1-thread-2] Preparando mazmorra 'Nido de Dragon' para el jugador Gandalf
...
```

**Lo que sucede:**
- Solo existen **3 hilos trabajadores** (pool-1-thread-1, pool-1-thread-2, pool-1-thread-3)
- Estos 3 hilos procesan **todas las 10 peticiones** de forma secuencial
- Cuando un hilo termina una tarea, **toma automaticamente la siguiente** de la cola
- Las peticiones esperan en cola hasta que un hilo este disponible

#### Los mismos hilos procesan varias peticiones → **reutilización de hilos**. ¿Qué significa esto?

**Respuesta:**

La **reutilizacion de hilos** significa que:

1. **Los hilos NO se destruyen** despues de completar una tarea
2. **El mismo hilo procesa multiples tareas** a lo largo de su vida
3. **Ahorro de recursos**: Crear y destruir hilos es costoso (tiempo de CPU, memoria)
4. **Mayor eficiencia**: Los hilos se mantienen vivos y listos para trabajar

**Ejemplo de la salida:**
```
[pool-1-thread-1] Mazmorra 'Catacumbas de Hyrule' lista para Link 🎮
[pool-1-thread-1] Preparando mazmorra 'Estrella de la Muerte' para Yennefer
```
El **mismo hilo** (pool-1-thread-1) procesa la peticion de Link y luego la de Yennefer.

#### ¿Que pasa si cambias el tamaño del pool a 1?

**Respuesta:**

Con `newFixedThreadPool(1)`:

```
=== SERVIDOR DE MAZMORRAS (Pool de 1 hilo) ===

[pool-2-thread-1] Preparando mazmorra 'Catacumbas de Hyrule' para el jugador Link
[pool-2-thread-1] Mazmorra 'Catacumbas de Hyrule' lista para Link 🎮
[pool-2-thread-1] Preparando mazmorra 'Torre Oscura' para el jugador Zelda
[pool-2-thread-1] Mazmorra 'Torre Oscura' lista para Zelda 🎮
[pool-2-thread-1] Preparando mazmorra 'Moria' para el jugador Geralt
...
```

**Observaciones:**
- **Procesamiento completamente secuencial**: Solo 1 jugador a la vez
- **Tiempo total mayor**: Si cada tarea tarda ~1-2 segundos, 10 tareas tardaran 10-20 segundos
- **No hay concurrencia**: Es como tener un solo empleado atendiendo a todos
- **Util para**: Tareas que deben ejecutarse en orden estricto

#### ¿Que pasa si cambias el tamaño del pool a 10?

**Respuesta:**

Con `newFixedThreadPool(10)`:

```
=== SERVIDOR DE MAZMORRAS (Pool de 10 hilos) ===

[pool-3-thread-1] Preparando mazmorra 'Catacumbas de Hyrule' para el jugador Link
[pool-3-thread-2] Preparando mazmorra 'Torre Oscura' para el jugador Zelda
[pool-3-thread-3] Preparando mazmorra 'Moria' para el jugador Geralt
[pool-3-thread-4] Preparando mazmorra 'Estrella de la Muerte' para el jugador Yennefer
[pool-3-thread-5] Preparando mazmorra 'Nido de Dragon' para el jugador Gandalf
[pool-3-thread-6] Preparando mazmorra 'Catacumbas de Hyrule' para el jugador Frodo
[pool-3-thread-7] Preparando mazmorra 'Torre Oscura' para el jugador Aragorn
[pool-3-thread-8] Preparando mazmorra 'Moria' para el jugador Leia
[pool-3-thread-9] Preparando mazmorra 'Estrella de la Muerte' para el jugador Luke
[pool-3-thread-10] Preparando mazmorra 'Nido de Dragon' para el jugador DarthVader
```

**Observaciones:**
- **Alta concurrencia**: Las 10 peticiones se procesan simultaneamente
- **Tiempo total minimo**: Todas terminan casi al mismo tiempo (~1-2 segundos)
- **Uso de recursos**: 10 hilos activos = mas memoria y cambios de contexto
- **Sobre-dimensionamiento**: Para 10 tareas, 10 hilos es exacto, pero normalmente se usa menos

---

## 3. Ejercicio 2 – ⚔️ Calculadora de Daño Crítico (Callable + Future)

### 3.1. Historia

Ahora estás en el **servidor de combate**.  
Cada jugador de la raid lanza ataques y el servidor tiene que calcular:

- Daño base.
- Si hay crítico o no (según probabilidad).
- Multiplicador de crítico.

Cada cálculo de ataque es una tarea que **devuelve el daño total**.  
Se ejecutan en paralelo usando un pool, y luego se suman los daños para ver el **DPS total de la raid**.

### 3.2. Qué vas a implementar

- Una clase `Ataque` con:
  - nombre del atacante
  - daño base
  - probabilidad de crítico
  - multiplicador crítico
- Una tarea `Callable<Integer>` que calcula el daño de un ataque.
- Un pool fijo (`newFixedThreadPool(4)`).
- Uso de `Future<Integer>` para leer el daño de cada ataque y sumarlo.

### 3.3. Código base

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CalculadoraDanoCritico {

    static class Ataque {
        final String atacante;
        final int danoBase;
        final double probCritico;          // Por ejemplo 0.25 = 25%
        final double multiplicadorCritico; // Por ejemplo 2.0 = x2

        Ataque(String atacante, int danoBase, double probCritico, double multiplicadorCritico) {
            this.atacante = atacante;
            this.danoBase = danoBase;
            this.probCritico = probCritico;
            this.multiplicadorCritico = multiplicadorCritico;
        }
    }

    static class TareaCalcularDano implements Callable<Integer> {
        private final Ataque ataque;

        TareaCalcularDano(Ataque ataque) {
            this.ataque = ataque;
        }

        @Override
        public Integer call() throws Exception {
            String hilo = Thread.currentThread().getName();
            System.out.println("[" + hilo + "] Calculando daño para " + ataque.atacante);

            boolean esCritico = Math.random() < ataque.probCritico;
            double multiplicador = esCritico ? ataque.multiplicadorCritico : 1.0;

            Thread.sleep(500 + (int)(Math.random() * 500));

            int danoFinal = (int) (ataque.danoBase * multiplicador);
            System.out.println("[" + hilo + "] " + ataque.atacante +
                    (esCritico ? " ¡CRÍTICO!" : " golpe normal") +
                    " -> daño: " + danoFinal);

            return danoFinal;
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        List<Future<Integer>> futuros = new ArrayList<>();

        // Simulamos una raid
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

        // Enviamos todas las tareas al pool
        for (Ataque ataque : ataques) {
            Future<Integer> futuro = pool.submit(new TareaCalcularDano(ataque));
            futuros.add(futuro);
        }

        // Recogemos resultados
        int totalRaid = 0;
        for (int i = 0; i < ataques.length; i++) {
            try {
                int dano = futuros.get(i).get(); 
                totalRaid += dano;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Lectura de resultado interrumpida");
            } catch (ExecutionException e) {
                System.out.println("Error calculando daño: " + e.getCause());
            }
        }

        System.out.println("Daño total de la raid: " + totalRaid);
        pool.shutdown();
    }
}
```

### 3.3. Responde y comenta la salida ejecutando los cambios que se proponen.

#### Diferencia: execute(Runnable) vs submit(Callable<V>)

**Respuesta:**

##### Con `execute(Runnable)` - NO hay valor de retorno

```java
=== PARTE 2: Usando Runnable (SIN valor de retorno) ===

[pool-2-thread-1] [RUNNABLE] Calculando daño para Caballero
[pool-2-thread-2] [RUNNABLE] Calculando daño para Druida
[pool-2-thread-3] [RUNNABLE] Calculando daño para Monje
[pool-2-thread-1] [RUNNABLE] Caballero -> daño: 200 (NO SE PUEDE RECOGER EL VALOR)
[pool-2-thread-2] [RUNNABLE] Druida -> daño: 195 (NO SE PUEDE RECOGER EL VALOR)
[pool-2-thread-3] [RUNNABLE] Monje -> daño: 95 (NO SE PUEDE RECOGER EL VALOR)

PROBLEMA: Con Runnable NO podemos sumar el daño total
   porque execute() no devuelve Future<V>
```

**Caracteristicas de execute(Runnable):**
- Metodo de firma: `void execute(Runnable command)`
- **NO devuelve nada** (void)
- Solo ejecuta codigo, no puede devolver resultados
- Util para: Tareas de "disparar y olvidar" (logging, notificaciones, etc.)
- **Imposible recuperar el resultado** del calculo

##### Con `submit(Callable<V>)` - SI hay valor de retorno

```java
=== PARTE 1: Usando Callable<Integer> (CON valor de retorno) ===

[pool-1-thread-1] Calculando daño para Mago del Fuego
[pool-1-thread-2] Calculando daño para Guerrero
[pool-1-thread-3] Calculando daño para Picaro
[pool-1-thread-4] Calculando daño para Arquera Elfica
[pool-1-thread-1] Mago del Fuego ¡CRITICO! -> daño: 300
[pool-1-thread-2] Guerrero golpe normal -> daño: 150
[pool-1-thread-3] Picaro ¡CRITICO! -> daño: 270
[pool-1-thread-4] Arquera Elfica golpe normal -> daño: 110
...

RESULTADOS RAID:
   Ataques procesados: 8
   Daño total: 1547
   Daño promedio: 193
```

**Caracteristicas de submit(Callable<V>):**
- Metodo de firma: `<T> Future<T> submit(Callable<T> task)`
- **Devuelve un Future<V>** que contendra el resultado
- Permite recuperar el valor de retorno con `future.get()`
- Util para: Calculos, busquedas, procesamiento de datos
- **Podemos sumar, promediar, y analizar los resultados**

**Tabla comparativa:**

| Caracteristica | execute(Runnable) | submit(Callable<V>) |
|----------------|-------------------|---------------------|
| Valor de retorno | ❌ void | ✅ Future<V> |
| Recuperar resultado | ❌ Imposible | ✅ future.get() |
| Manejo de excepciones | Solo con try-catch interno | ✅ ExecutionException |
| Uso tipico | Logs, notificaciones | Calculos, consultas |

####  Cómo se pueden lanzar muchos cálculos de daño en paralelo y luego recogerlos todos.

**Respuesta:**

El patron es el siguiente:

```java
// 1. Crear el pool
ExecutorService pool = Executors.newFixedThreadPool(4);
List<Future<Integer>> futuros = new ArrayList<>();

// 2. Enviar TODAS las tareas al pool (se ejecutan en paralelo)
for (Ataque ataque : ataques) {
    Future<Integer> futuro = pool.submit(new TareaCalcularDano(ataque));
    futuros.add(futuro);  // Guardamos cada Future
}

// 3. Recoger TODOS los resultados
int totalRaid = 0;
for (Future<Integer> futuro : futuros) {
    int dano = futuro.get();  // Espera si es necesario
    totalRaid += dano;
}
```

**Ventajas de este patron:**
1. **Paralelismo real**: Los 8 ataques se calculan simultaneamente (limitado por el pool de 4)
2. **No bloqueante en el envio**: `submit()` retorna inmediatamente
3. **Bloqueante en la lectura**: `get()` espera si el resultado no esta listo
4. **Recoleccion ordenada**: Puedes recoger en el orden que quieras

**Salida mostrando paralelismo:**
```
[pool-1-thread-1] Calculando daño para Mago del Fuego
[pool-1-thread-2] Calculando daño para Guerrero      <- Al mismo tiempo
[pool-1-thread-3] Calculando daño para Picaro        <- Al mismo tiempo
[pool-1-thread-4] Calculando daño para Arquera Elfica <- Al mismo tiempo
```

#### Probar a cambiar la probabilidad de crítico y ver cómo sube/baja el daño total.

**Respuesta:**

```java
=== PARTE 3: Comparacion con diferentes probabilidades ===

--- Probabilidad de critico: 10% ---
[pool-4-thread-1] Atacante 1 golpe normal -> daño: 100
[pool-4-thread-2] Atacante 2 golpe normal -> daño: 100
[pool-4-thread-3] Atacante 3 ¡CRITICO! -> daño: 200
[pool-4-thread-1] Atacante 4 golpe normal -> daño: 100
[pool-4-thread-2] Atacante 5 golpe normal -> daño: 100
   Daño total con probabilidad 10%: 600

--- Probabilidad de critico: 30% ---
[pool-5-thread-1] Atacante 1 ¡CRITICO! -> daño: 200
[pool-5-thread-2] Atacante 2 golpe normal -> daño: 100
[pool-5-thread-3] Atacante 3 ¡CRITICO! -> daño: 200
[pool-5-thread-1] Atacante 4 golpe normal -> daño: 100
[pool-5-thread-2] Atacante 5 ¡CRITICO! -> daño: 200
   Daño total con probabilidad 30%: 800

--- Probabilidad de critico: 50% ---
[pool-6-thread-1] Atacante 1 ¡CRITICO! -> daño: 200
[pool-6-thread-2] Atacante 2 ¡CRITICO! -> daño: 200
[pool-6-thread-3] Atacante 3 golpe normal -> daño: 100
[pool-6-thread-1] Atacante 4 ¡CRITICO! -> daño: 200
[pool-6-thread-2] Atacante 5 golpe normal -> daño: 100
   Daño total con probabilidad 50%: 800

--- Probabilidad de critico: 80% ---
[pool-7-thread-1] Atacante 1 ¡CRITICO! -> daño: 200
[pool-7-thread-2] Atacante 2 ¡CRITICO! -> daño: 200
[pool-7-thread-3] Atacante 3 ¡CRITICO! -> daño: 200
[pool-7-thread-1] Atacante 4 ¡CRITICO! -> daño: 200
[pool-7-thread-2] Atacante 5 ¡CRITICO! -> daño: 200
   Daño total con probabilidad 80%: 1000
```

**Analisis:**

| Probabilidad Critico | Daño Base (5 ataques) | Daño Real Observado | % Incremento |
|---------------------|----------------------|---------------------|--------------|
| 10% | 500 | ~550-650 | ~10-30% |
| 30% | 500 | ~650-850 | ~30-70% |
| 50% | 500 | ~750-1000 | ~50-100% |
| 80% | 500 | ~900-1000 | ~80-100% |

**Conclusiones:**
1. A mayor probabilidad de critico, mayor daño total esperado
2. Con 80% de critico, casi todos los golpes son x2
3. La varianza es alta con pocas muestras (solo 5 ataques)

---

## 4. Ejercicio 3 – 👹 Spawns de Enemigos en Mundo Abierto (ScheduledExecutorService)

### 4.1. Historia

Ahora tienes un **mundo abierto** tipo sandbox. Cada pocos segundos, el juego debe:

- Lanzar spawns de enemigos en distintas zonas.
- Loguear qué aparece y dónde.

Usarás un `ScheduledExecutorService` como si fuera el motor que programa los spawns.

### 4.2. Qué vas a implementar

- Una tarea `Runnable` que genera:
  - Una zona aleatoria.
  - Un enemigo aleatorio.
- Un `ScheduledExecutorService` que ejecuta esta tarea **cada 2 segundos**.
- Un `main` que deja el sistema funcionando unos segundos y luego apaga el scheduler.

### 4.3. Código base sugerido

```java
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpawnsMundoAbierto {

    static class SpawnTarea implements Runnable {

        private final String[] zonas = {
                "Bosque Maldito",
                "Ruinas Antiguas",
                "Pantano Radiactivo",
                "Ciudad Cibernética",
                "Templo Prohibido"
        };

        private final String[] enemigos = {
                "Slime Mutante",
                "Esqueleto Guerrero",
                "Mecha-Dragón",
                "Bandido del Desierto",
                "Lich Supremo"
        };

        @Override
        public void run() {
            String hilo = Thread.currentThread().getName();
            String zona = zonas[(int)(Math.random() * zonas.length)];
            String enemigo = enemigos[(int)(Math.random() * enemigos.length)];
            System.out.println("[" + LocalTime.now() + "][" + hilo + "] Spawn de " +
                    enemigo + " en " + zona);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // Ejecutar la tarea cada 2 segundos, sin retraso inicial
        scheduler.scheduleAtFixedRate(new SpawnTarea(), 0, 2, TimeUnit.SECONDS);

        // Dejamos que el mundo “viva” durante 12 segundos
        Thread.sleep(12000);

        // Apagado ordenado
        System.out.println("Deteniendo spawns...");
        scheduler.shutdown();
        if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("Forzando parada de spawns.");
            scheduler.shutdownNow();
        }
        System.out.println("Servidor de mundo abierto detenido.");
    }
}
```

### 4.3. Responde y comenta la salida ejecutando los cambios que se proponen.

#### schedule(...) - Ejecucion unica en el futuro

**Respuesta:**

`schedule(Runnable task, long delay, TimeUnit unit)` ejecuta una tarea **UNA SOLA VEZ** despues de un retraso.

```java
=== SPAWN UNICO RETRASADO (despues de 3 segundos) ===

Programando spawn de boss...

[Esperando 3 segundos...]

============================================================
¡ALERTA DE BOSS!
   Dragon Ancestral ha aparecido en Volcan Oscuro
   ¡Todos los jugadores, preparense para el combate!
============================================================
```

**Caracteristicas:**
- **Ejecucion unica**: Solo se ejecuta una vez
- **Con retraso**: Espera el tiempo especificado antes de ejecutar
- **No se repite**: Despues de ejecutarse, termina
- **Uso tipico**: 
  - Eventos especiales programados
  - Timeouts
  - Recordatorios
  - Bosses que aparecen en momentos especificos

**Codigo de ejemplo:**
```java
scheduler.schedule(
    new SpawnBossTarea(TipoEnemigo.DRAGON_ANCESTRAL, Zona.VOLCAN_OSCURO),
    3,  // delay de 3 segundos
    TimeUnit.SECONDS
);
```

#### scheduleAtFixedRate(...) - Ejecucion repetida periodicamente

**Respuesta:**

`scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit)` ejecuta una tarea **REPETIDAMENTE** cada X tiempo.

```java
=== SPAWNS NORMALES (cada 2 segundos) ===

[14:23:10.123][pool-1-thread-1] Spawn #1: Comun Slime Mutante (Nv.10) en Bosque Maldito
[14:23:12.125][pool-1-thread-1] Spawn #2: Comun Goblin Saqueador (Nv.12) en Ruinas Antiguas
[14:23:14.127][pool-1-thread-1] Spawn #3: Poco Comun Bandido del Desierto (Nv.25) en Pantano Radiactivo
[14:23:16.129][pool-1-thread-1] Spawn #4: Comun Esqueleto Guerrero (Nv.15) en Ciudad Cibernetica
[14:23:18.131][pool-1-thread-1] Spawn #5: Raro Mecha-Dragon (Nv.50) en Templo Prohibido
[14:23:20.133][pool-1-thread-1] Spawn #6: Comun Zombie Infectado (Nv.18) en Volcan Oscuro

Deteniendo spawns normales...
Total de spawns generados: 6
```

**Caracteristicas:**
- **Ejecucion repetida**: Se ejecuta periodicamente
- **Periodo fijo**: Cada X segundos/minutos/horas
- **Inicial delay**: Puede empezar inmediatamente (0) o con retraso
- **Continua hasta shutdown**: No para hasta que se llame `shutdown()`
- **Uso tipico**:
  - Spawns de enemigos
  - Actualizaciones periodicas
  - Heartbeats
  - Verificaciones de estado

**Codigo de ejemplo:**
```java
scheduler.scheduleAtFixedRate(
    new SpawnTarea(0, true),
    0,        // sin retraso inicial
    2,        // cada 2 segundos
    TimeUnit.SECONDS
);
```

**Diferencias clave:**

| Aspecto | schedule() | scheduleAtFixedRate() |
|---------|-----------|----------------------|
| Ejecuciones | 1 sola vez | Infinitas (hasta shutdown) |
| Periodicidad | No | Si, cada X tiempo |
| Delay inicial | Si | Si (opcional) |
| Uso | Eventos unicos | Procesos continuos |

#### Cómo se comporta el sistema si la tarea tarda más que el período. Modifca, muestra el resultado y comenta.

**Respuesta:**

```java
=== SPAWNS LENTOS (tarea tarda 3s, periodo 2s) ===
Observa como se acumulan los spawns si tardan mas que el periodo

[14:23:30.001][pool-2-thread-1] Spawn #1: SLIME_MUTANTE en BOSQUE_MALDITO
Spawn tardo 3001ms
[14:23:33.005][pool-2-thread-1] Spawn #2: GOBLIN_SAQUEADOR en RUINAS_ANTIGUAS
Spawn tardo 3002ms
[14:23:36.010][pool-2-thread-1] Spawn #3: ZOMBIE_INFECTADO en PANTANO_RADIACTIVO
Spawn tardo 3001ms
[14:23:39.015][pool-2-thread-1] Spawn #4: ESQUELETO_GUERRERO en CIUDAD_CIBERNETICA
Spawn tardo 3003ms

Deteniendo spawns lentos...
Total de spawns iniciados: 4
Nota: Las tareas NO se solapan, esperan a que termine la anterior
```

**¿Que sucede?**

1. **Primera ejecucion**: Empieza en t=0, tarda 3 segundos
2. **Segunda ejecucion**: Deberia empezar en t=2, pero la primera no ha terminado
   - **Se retrasa** hasta t=3 (cuando termina la primera)
3. **Tercera ejecucion**: Deberia empezar en t=4, pero la segunda no ha terminado
   - **Se retrasa** hasta t=6
4. **Las tareas NO se solapan**: `scheduleAtFixedRate` espera a que termine la anterior

**Diagrama temporal:**

```
Tiempo:     0s    2s    4s    6s    8s    10s   12s
Periodo:    |-----|-----|-----|-----|-----|-----|
            ^           ^           ^           ^
            |           |           |           |
Ejecucion:  [===Spawn1===]
                        [===Spawn2===]
                                    [===Spawn3===]
                                                [===Spawn4===]
```

**Consecuencias:**
- El periodo efectivo se convierte en el **tiempo de la tarea** (3s en vez de 2s)
- No hay paralelismo si hay 1 hilo
- Las ejecuciones se retrasan progresivamente
- **Solucion**: Aumentar el pool size o optimizar la tarea

#### Probar a cambiar el período (1s, 3s…) y la duración del `sleep` del `main`.  Modifca, muestra el resultado y comenta.

**Respuesta:**

```java
=== COMPARACION DE PERIODOS ===

--- Spawns cada 1 segundo(s) ---
[14:24:00.001][pool-3-thread-1] Spawn #1: SLIME_MUTANTE en BOSQUE_MALDITO
[14:24:01.005][pool-3-thread-1] Spawn #2: GOBLIN_SAQUEADOR en RUINAS_ANTIGUAS
[14:24:02.008][pool-3-thread-1] Spawn #3: ZOMBIE_INFECTADO en PANTANO_RADIACTIVO
[14:24:03.011][pool-3-thread-1] Spawn #4: ESQUELETO_GUERRERO en CIUDAD_CIBERNETICA
[14:24:04.014][pool-3-thread-1] Spawn #5: BANDIDO_DEL_DESIERTO en TEMPLO_PROHIBIDO
[14:24:05.017][pool-3-thread-1] Spawn #6: MECHA_SOLDADO en VOLCAN_OSCURO
   Spawns en 6 segundos: 6

--- Spawns cada 3 segundo(s) ---
[14:24:07.001][pool-4-thread-1] Spawn #1: ESPECTRO_OSCURO en DESIERTO_CARMESI
[14:24:10.005][pool-4-thread-1] Spawn #2: MECHA_DRAGON en CRIPTA_SUBTERRANEA
   Spawns en 6 segundos: 2

--- Spawns cada 5 segundo(s) ---
[14:24:13.001][pool-5-thread-1] Spawn #1: LICH_SUPREMO en BOSQUE_MALDITO
   Spawns en 6 segundos: 1
```

**Analisis por periodo:**

| Periodo | Spawns en 6s | Calculo | Observacion |
|---------|-------------|---------|-------------|
| 1 segundo | 6 | 6/1 = 6 | Alta frecuencia, mundo muy dinamico |
| 3 segundos | 2 | 6/3 = 2 | Frecuencia media, equilibrado |
| 5 segundos | 1 | 6/5 = 1.2 ≈ 1 | Baja frecuencia, mundo tranquilo |

**Formula general:**
```
Numero de spawns = (duracion_total / periodo) + 1
```
(El +1 es por la ejecucion inicial en t=0)

**Recomendaciones por tipo de juego:**
- **MMO con muchos jugadores**: 1-2 segundos (alta densidad)
- **Juego survival/horror**: 5-10 segundos (tension, escasez)
- **Sandbox casual**: 3-5 segundos (equilibrio)
- **Boss fights**: schedule() unico en vez de repetido

---

## 5. Retos opcionales para subir de nivel 🧙‍♂️

- **Reto 1:**  
  Añade tiempo de cola / prioridad de jugadores en el servidor de mazmorras.
- **Reto 2:**  
  Haz que cada ataque pueda fallar (daño 0) y calcula la **media** de daño.
- **Reto 3:**  
  Añade tipos de spawn “raro” (enemigo épico) con menos probabilidad.
- **Reto 4:**  
  Usa constantes y enums (`enum`) para las zonas, enemigos, clases de personaje, etc.
- **Reto 5:**  
  Escribe tus propias trazas de log con formato tipo:
  `[TIMESTAMP] [SISTEMA] mensaje`.

## Licencia 📄

Este proyecto está bajo la Licencia (Apache 2.0) - mira el archivo [LICENSE.md]([../../../LICENSE.md](https://github.com/jpexposito/code-learn-practice/blob/main/LICENSE)) para detalles.