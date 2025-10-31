
package org.formacion.procesos.services.abstractas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.formacion.procesos.domain.Job;
import org.formacion.procesos.repositories.interfaces.IJobRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author danielrguezh
 * @version 1.0.0
 */

public abstract class ComandoServiceAbstract {
    private String comando;
    private Job tipo;
    private String validacion;
    private IJobRepository fileRepository;

    /**
     * Getters and Setters
     */
    public IJobRepository getFileRepository() {
        return fileRepository;
    }

    @Autowired
    public void setFileRepository(IJobRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public Job getTipo() {
        return tipo;
    }

    public String getTipoToString() {
        if (tipo == null) {
            return null;
        }
        return tipo.toString();
    }

    public void setTipo(Job tipo) {
        this.tipo = tipo;
    }

    public String getValidacion() {
        return validacion;
    }

    public void setValidacion(String validacion) {
        this.validacion = validacion;
    }

    /**
     * Metodo que manda a procesar el comando obtenido
     * @param linea a procesar
     */
    public void procesarLinea(String linea) {
        String[] arrayComando = linea.split("\s+");
        this.setComando(arrayComando[0]);
        if (!validar(arrayComando)) {
            return;
        }
        try {
            Process proceso = new ProcessBuilder("sh", "-c", linea).start();
            Path outputFile = fileRepository.getPath();
            Path errorFile = Paths.get(outputFile.toString().replace(".txt", "_err.txt"));
            ejecutarProceso(proceso, outputFile, errorFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que imprime por consola el [OUT] o el [ERR] obtenido 
     * y lo escribe en el fichero correspondiente, y manda a ejecutar el proceso
     * @param proceso a ejecutar
     * @param outputPath ruta del fichero out
     * @param errorPath ruta del fichero de err
     * @return true/false
     */
    public boolean ejecutarProceso(Process proceso, Path outputPath, Path errorPath) {
        try (OutputStream outStream = Files.newOutputStream(outputPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            OutputStream errStream = Files.newOutputStream(errorPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            BufferedReader outReader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            BufferedReader errReader = new BufferedReader(new InputStreamReader(proceso.getErrorStream()))) {

            String outLine;
            String errLine = null;

            while ((outLine = outReader.readLine()) != null || (errLine = errReader.readLine()) != null) {
                if (outLine != null) {
                    System.out.println("[OUT] " + outLine);
                    outStream.write((outLine + "\n").getBytes());
                }
                if (errLine != null) {
                    System.err.println("[ERR] " + errLine);
                    errStream.write((errLine + "\n").getBytes());
                }
            }

            int exitCode = proceso.waitFor();
            return exitCode == 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo para validar si el parametro añadido al comando es valido o no
     * @param arrayComando a comprobar
     * @return true/false
     */
    public boolean validar(String[] arrayComando) {
        if (!validarComando()) {
            return false;
        }
        String parametro = arrayComando[1];
        Pattern pattern = Pattern.compile(validacion);
        Matcher matcher = pattern.matcher(parametro);
        if (!matcher.find()) {
            System.out.println("No cumple");
            return false;
        }
        return true;
    }

    /**
     * Metodo para validar si el comando es valido o no
     * @return true/false
     */
    public boolean validarComando() {
        if (!this.getComando().toUpperCase().equals(getTipoToString())) {
            System.out.println("El comando es invalido");
            return false;
        }
        return true;
    }
}
