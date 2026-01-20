package com.docencia.tasks.adapters.in.api;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class TaskResponseTest {

    @Test
    void constructorVacio_shouldCreateObject() {
        TaskResponse tr = new TaskResponse();
        assertThat(tr).isNotNull();
    }

    @Test
    void constructorConId_shouldSetId() {
        TaskResponse tr = new TaskResponse(5L);
        assertThat(tr.getId()).isEqualTo(5L);
    }

    @Test
    void constructorCompleto_shouldSetAllFields() {
        TaskResponse tr = new TaskResponse(10L, "Titulo", "Descripcion", true);
        assertThat(tr.getId()).isEqualTo(10L);
        assertThat(tr.getTitle()).isEqualTo("Titulo");
        assertThat(tr.getDescription()).isEqualTo("Descripcion");
        assertThat(tr.isCompleted()).isTrue();
    }

    @Test
    void settersAndGetters_shouldWork() {
        TaskResponse tr = new TaskResponse();
        tr.setId(10L);
        tr.setTitle("Titulo");
        tr.setDescription("Descripcion");
        tr.setCompleted(false);

        assertThat(tr.getId()).isEqualTo(10L);
        assertThat(tr.getTitle()).isEqualTo("Titulo");
        assertThat(tr.getDescription()).isEqualTo("Descripcion");
        assertThat(tr.isCompleted()).isFalse();
        assertThat(tr.getCompleted()).isFalse();
    }

    @Test
    void equals_sameId_shouldBeEqual() {
        TaskResponse tr1 = new TaskResponse(10L, "A", "B", true);
        TaskResponse tr2 = new TaskResponse(10L, "X", "Y", false);

        assertThat(tr1).isEqualTo(tr2);
    }

    @Test
    void equals_differentId_shouldNotBeEqual() {
        TaskResponse tr1 = new TaskResponse(10L, "A", "B", true);
        TaskResponse tr2 = new TaskResponse(20L, "A", "B", true);

        assertThat(tr1).isNotEqualTo(tr2);
    }

    @Test
    void equals_nullOrOtherType_shouldNotBeEqual() {
        TaskResponse tr = new TaskResponse(10L, "A", "B", true);

        assertThat(tr).isNotEqualTo(null);
        assertThat(tr).isNotEqualTo("string");
    }

    @Test
    void hashCode_sameId_shouldBeEqual() {
        TaskResponse tr1 = new TaskResponse(10L);
        TaskResponse tr2 = new TaskResponse(10L);

        assertThat(tr1.hashCode()).isEqualTo(tr2.hashCode());
    }

    @Test
    void hashCode_differentId_shouldNotBeEqual() {
        TaskResponse tr1 = new TaskResponse(10L);
        TaskResponse tr2 = new TaskResponse(20L);

        assertThat(tr1.hashCode()).isNotEqualTo(tr2.hashCode());
    }
}