package org.formacion.procesos.repositories.file;

import org.formacion.procesos.repositories.interfaces.IJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author danielrguezh
 * @version 1.0.0
 */

@Repository
public class FileJobRepository implements IJobRepository{

    private static Logger logger = LoggerFactory.getLogger(FileJobRepository.class);
    String fileName;
    static Path path;

    /**
     * Getters and Setters
     */
    @Override
    public Path getPath() {
        return path;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Constructor por defecto
     */
    public FileJobRepository() {
        if (fileName == null) {
            fileName = "mis_procesos.txt";
        }
        URL resource = getClass().getClassLoader().getResource(fileName);
        path = Paths.get(resource.getPath());
    }

    /**
     * Metodo para agregar contenido al fichero
     * @param text a agregar
     * @return true/false
     */
    @Override
    public boolean add(String text) {
        try {
            Files.write(path, text.getBytes(), StandardOpenOption.APPEND);
            return true;
        } catch (Exception e) {
            logger.error("Se ha producido un error almacenando en el fichero ", e);
        }
        return false;
    }
}