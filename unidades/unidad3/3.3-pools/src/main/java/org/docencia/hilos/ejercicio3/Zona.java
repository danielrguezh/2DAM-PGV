package org.docencia.hilos.ejercicio3;

import java.util.Random;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

/**
 * Enum para las diferentes zonas del mundo abierto
 */
public enum Zona {
    BOSQUE_MALDITO("Bosque Maldito"),
    RUINAS_ANTIGUAS("Ruinas Antiguas"),
    PANTANO_RADIACTIVO("Pantano Radiactivo"),
    CIUDAD_CIBERNETICA("Ciudad Cibernética"),
    TEMPLO_PROHIBIDO("Templo Prohibido"),
    VOLCAN_OSCURO("Volcán Oscuro"),
    DESIERTO_CARMESI("Desierto Carmesí"),
    CRIPTA_SUBTERRANEA("Cripta Subterránea");

    private final String nombre;
    private static final Random random = new Random();

    Zona(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public static Zona aleatoria() {
        Zona[] zonas = values();
        return zonas[random.nextInt(zonas.length)];
    }
}
