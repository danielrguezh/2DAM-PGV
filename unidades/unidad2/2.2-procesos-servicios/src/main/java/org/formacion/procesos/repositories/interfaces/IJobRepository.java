package org.formacion.procesos.repositories.interfaces;

import java.nio.file.Path;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
public interface IJobRepository {
    public boolean add(String text);
    public Path getPath();
}