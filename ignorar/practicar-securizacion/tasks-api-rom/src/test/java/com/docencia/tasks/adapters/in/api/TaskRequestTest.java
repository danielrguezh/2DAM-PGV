package com.docencia.tasks.adapters.in.api;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class TaskRequestTest {

    @Test
    void constructorVacio_shouldCreateObject() {
        TaskRequest tr = new TaskRequest();
        assertThat(tr).isNotNull();
    }

    @Test
    void constructorCompleto_shouldSetAllFields() {
        TaskRequest tr = new TaskRequest("Titulo", "Descripcion", true);
        assertThat(tr.getTitle()).isEqualTo("Titulo");
        assertThat(tr.getDescription()).isEqualTo("Descripcion");
        assertThat(tr.getCompleted()).isTrue();
    }

    @Test
    void settersAndGetters_shouldWork() {
        TaskRequest tr = new TaskRequest();
        tr.setTitle("Nuevo Titulo");
        tr.setDescription("Nueva Descripcion");
        tr.setCompleted(false);

        assertThat(tr.getTitle()).isEqualTo("Nuevo Titulo");
        assertThat(tr.getDescription()).isEqualTo("Nueva Descripcion");
        assertThat(tr.getCompleted()).isFalse();
    }

    @Test
    void isCompleted_shouldReturnValue() {
        TaskRequest tr = new TaskRequest();
        tr.setCompleted(true);
        assertThat(tr.isCompleted()).isTrue();

        tr.setCompleted(false);
        assertThat(tr.isCompleted()).isFalse();
    }
}