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
        TaskResponse tr = new TaskResponse(1L, "Title", "Desc", true);
        assertThat(tr.getId()).isEqualTo(1L);
        assertThat(tr.getTitle()).isEqualTo("Title");
        assertThat(tr.getDescription()).isEqualTo("Desc");
        assertThat(tr.isCompleted()).isTrue();
    }

    @Test
    void settersAndGetters_shouldWork() {
        TaskResponse tr = new TaskResponse();
        tr.setId(10L);
        tr.setTitle("T");
        tr.setDescription("D");
        tr.setCompleted(false);

        assertThat(tr.getId()).isEqualTo(10L);
        assertThat(tr.getTitle()).isEqualTo("T");
        assertThat(tr.getDescription()).isEqualTo("D");
        assertThat(tr.isCompleted()).isFalse();
        assertThat(tr.getCompleted()).isFalse();
    }

    @Test
    void equals_sameId_shouldBeEqual() {
        TaskResponse tr1 = new TaskResponse(1L, "A", "B", true);
        TaskResponse tr2 = new TaskResponse(1L, "X", "Y", false);

        assertThat(tr1).isEqualTo(tr2);
    }

    @Test
    void equals_differentId_shouldNotBeEqual() {
        TaskResponse tr1 = new TaskResponse(1L, "A", "B", true);
        TaskResponse tr2 = new TaskResponse(2L, "A", "B", true);

        assertThat(tr1).isNotEqualTo(tr2);
    }

    @Test
    void equals_nullOrOtherType_shouldNotBeEqual() {
        TaskResponse tr = new TaskResponse(1L, "A", "B", true);

        assertThat(tr).isNotEqualTo(null);
        assertThat(tr).isNotEqualTo("string");
    }

    @Test
    void hashCode_sameId_shouldBeEqual() {
        TaskResponse tr1 = new TaskResponse(1L);
        TaskResponse tr2 = new TaskResponse(1L);

        assertThat(tr1.hashCode()).isEqualTo(tr2.hashCode());
    }

    @Test
    void hashCode_differentId_shouldNotBeEqual() {
        TaskResponse tr1 = new TaskResponse(1L);
        TaskResponse tr2 = new TaskResponse(2L);

        assertThat(tr1.hashCode()).isNotEqualTo(tr2.hashCode());
    }
}