package org.docencia.hilos.ejercicio3;

public enum Rareza {
    COMUN("Común", 60.0),
    POCO_COMUN("Poco Común", 25.0),
    RARO("Raro", 10.0),
    EPICO("Épico", 4.0),
    LEGENDARIO("Legendario", 1.0);

    private final String nombre;
    private final double probabilidad;

    Rareza(String nombre, double probabilidad) {
        this.nombre = nombre;
        this.probabilidad = probabilidad;
    }

    public String getNombre() {
        return nombre;
    }

    public double getProbabilidad() {
        return probabilidad;
    }
}
