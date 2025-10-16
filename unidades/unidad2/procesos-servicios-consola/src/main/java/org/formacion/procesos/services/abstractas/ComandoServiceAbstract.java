package org.formacion.procesos.services.abstractas;

import java.util.List;

import org.formacion.procesos.domain.ProcessType;
import org.formacion.procesos.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ComandoServiceAbstract {
    String comando;
    List<String> parametros;
    ProcessType tipo;

    @Autowired
    FileRepository fileRepository;

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
        if (!validar(arrayComando)) {
            System.out.println("Comando invalido");
            return;
        }
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

    public boolean validarComando(){
        if (!this.getComando().toUpperCase().equals(getTipoString())) {
            System.out.println("Comando invalido");
            return false;
        }
        return true;
    }

    public abstract boolean validar(String[] arrayComando);
}