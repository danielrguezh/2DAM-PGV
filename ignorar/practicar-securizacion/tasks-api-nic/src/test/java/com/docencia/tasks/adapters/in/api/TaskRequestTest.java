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
        TaskRequest tr = new TaskRequest("Title", "Desc", true);
        assertThat(tr.getTitle()).isEqualTo("Title");
        assertThat(tr.getDescription()).isEqualTo("Desc");
        assertThat(tr.getCompleted()).isTrue();
    }

    @Test
    void settersAndGetters_shouldWork() {
        TaskRequest tr = new TaskRequest();
        tr.setTitle("New Title");
        tr.setDescription("New Desc");
        tr.setCompleted(false);

        assertThat(tr.getTitle()).isEqualTo("New Title");
        assertThat(tr.getDescription()).isEqualTo("New Desc");
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