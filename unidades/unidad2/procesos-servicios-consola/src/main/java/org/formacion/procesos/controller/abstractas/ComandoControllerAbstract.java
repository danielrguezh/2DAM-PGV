package org.formacion.procesos.controller.abstractas;

import java.util.List;

import org.formacion.procesos.domain.ProcessType;

public abstract class ComandoControllerAbstract {
    String comando;
    List<String> parametros;
    ProcessType tipo;

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public List getParametros() {
        return parametros;
    }

    public void setParametros(List parametros) {
        this.parametros = parametros;
    }


    public void procesarLinea(String linea){
        String[] arrayComando = linea.split(" ");
        this.setComando(arrayComando[0]);
        System.out.println("Comando: " +this.getComando());
        Process proceso;
        try{
            proceso = new ProcessBuilder("sh", "-c", linea+ " > mis_procesos.txt").start();
           ejecutarProceso(proceso);
        } catch(Exception e){
            e.printStackTrace();
        }
        imprimeMensaje();
    }


    public boolean ejecutarProceso(Process proceso){
        try{
            proceso.waitFor();
        } catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }


    public ProcessType getTipo() {
        return this.tipo;
    }

    public String getTipoString() {
        if (tipo==null) {
            return null;
        }
        return this.toString();
    }

    public void setTipo(ProcessType tipo) {
        this.tipo = tipo;
    }

    public abstract void imprimeMensaje();
}