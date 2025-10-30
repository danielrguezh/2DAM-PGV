package org.formacion.procesos.repositories.interfaces;

import java.nio.file.Path;

public interface IJobRepository {
    public boolean add(String text);
    public Path getPath();
}