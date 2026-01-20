package com.docencia.tasks.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    void constructor_withId_shouldSetId() {
        Task task = new Task(10L);
        assertThat(task.getId()).isEqualTo(10L);
    }

    @Test
    void equals_shouldReturnTrueForSameObject() {
        Task task = new Task(1L);
        assertThat(task.equals(task)).isTrue(); // mismo objeto
    }

    @Test
    void equals_shouldReturnTrueForSameId() {
        Task task1 = new Task(1L);
        Task task2 = new Task(1L);

        assertThat(task1.equals(task2)).isTrue();
        assertThat(task2.equals(task1)).isTrue();
    }

    @Test
    void equals_shouldReturnFalseForDifferentId() {
        Task task1 = new Task(1L);
        Task task2 = new Task(2L);

        assertThat(task1.equals(task2)).isFalse();
        assertThat(task2.equals(task1)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        Task task = new Task(1L);
        assertThat(task.equals(null)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseForDifferentClass() {
        Task task = new Task(1L);
        String other = "not a Task";

        assertThat(task.equals(other)).isFalse();
    }

    @Test
    void hashCode_shouldBeSameForSameId() {
        Task task1 = new Task(1L);
        Task task2 = new Task(1L);

        assertThat(task1.hashCode()).isEqualTo(task2.hashCode());
    }

    @Test
    void hashCode_shouldBeDifferentForDifferentId() {
        Task task1 = new Task(1L);
        Task task2 = new Task(2L);

        assertThat(task1.hashCode()).isNotEqualTo(task2.hashCode());
    }
}
