package org.docencia.hilos.ejercicio3;

import java.util.Arrays;
import java.util.Random;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

/**
 * Enum para los diferentes tipos de enemigos
 */
public enum TipoEnemigo {
    SLIME_MUTANTE("Slime Mutante", Rareza.COMUN, 10),
    ESQUELETO_GUERRERO("Esqueleto Guerrero", Rareza.COMUN, 15),
    GOBLIN_SAQUEADOR("Goblin Saqueador", Rareza.COMUN, 12),
    ZOMBIE_INFECTADO("Zombie Infectado", Rareza.COMUN, 18),

    BANDIDO_DEL_DESIERTO("Bandido del Desierto", Rareza.POCO_COMUN, 25),
    MECHA_SOLDADO("Mecha-Soldado", Rareza.POCO_COMUN, 30),
    ESPECTRO_OSCURO("Espectro Oscuro", Rareza.POCO_COMUN, 28),

    MECHA_DRAGON("Mecha-Dragón", Rareza.RARO, 50),
    LICH_SUPREMO("Lich Supremo", Rareza.RARO, 60),
    DEMONIO_ANCESTRAL("Demonio Ancestral", Rareza.RARO, 55),

    DRAGON_ANCESTRAL("Dragón Ancestral", Rareza.EPICO, 100),
    REY_LICH("Rey Lich", Rareza.EPICO, 120),

    GUARDIAN_PRIMORDIAL("Guardián Primordial", Rareza.LEGENDARIO, 200),
    DIOS_OLVIDADO("Dios Olvidado", Rareza.LEGENDARIO, 300);

    private final String nombre;
    private final Rareza rareza;
    private final int nivel;
    private static final Random random = new Random();

    TipoEnemigo(String nombre, Rareza rareza, int nivel) {
        this.nombre = nombre;
        this.rareza = rareza;
        this.nivel = nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public Rareza getRareza() {
        return rareza;
    }

    public int getNivel() {
        return nivel;
    }

    /**
     * Genera un enemigo aleatorio basado en probabilidades de rareza
     */
    public static TipoEnemigo aleatorioConProbabilidad() {
        double roll = random.nextDouble() * 100;

        // Probabilidades: 60% común, 25% poco común, 10% raro, 4% épico, 1% legendario
        Rareza rarezaObjetivo;
        if (roll < 60) {
            rarezaObjetivo = Rareza.COMUN;
        } else if (roll < 85) {
            rarezaObjetivo = Rareza.POCO_COMUN;
        } else if (roll < 95) {
            rarezaObjetivo = Rareza.RARO;
        } else if (roll < 99) {
            rarezaObjetivo = Rareza.EPICO;
        } else {
            rarezaObjetivo = Rareza.LEGENDARIO;
        }

        TipoEnemigo[] candidatos = Arrays.stream(values())
                .filter(e -> e.getRareza() == rarezaObjetivo)
                .toArray(TipoEnemigo[]::new);

        return candidatos[random.nextInt(candidatos.length)];
    }

    @Override
    public String toString() {
        return rareza.getNombre() + " " + nombre + " (Nv." + nivel + ")";
    }
}
