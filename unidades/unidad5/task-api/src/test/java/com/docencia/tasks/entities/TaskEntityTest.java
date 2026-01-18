package com.docencia.tasks.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskEntityTest {

    @Test
    void defaultConstructor_shouldCreateEmptyEntity() {
        TaskEntity task = new TaskEntity();

        assertNull(task.getId());
        assertNull(task.getTitle());
        assertNull(task.getDescription());
        assertFalse(task.isCompleted());
    }

    @Test
    void constructorWithoutId_shouldSetFieldsCorrectly() {
        TaskEntity task = new TaskEntity(
                "Título",
                "Descripción",
                true
        );

        assertNull(task.getId());
        assertEquals("Título", task.getTitle());
        assertEquals("Descripción", task.getDescription());
        assertTrue(task.isCompleted());
    }

    @Test
    void constructorWithId_shouldSetAllFieldsCorrectly() {
        TaskEntity task = new TaskEntity(
                1L,
                "Título",
                "Descripción",
                false
        );

        assertEquals(1L, task.getId());
        assertEquals("Título", task.getTitle());
        assertEquals("Descripción", task.getDescription());
        assertFalse(task.isCompleted());
    }

    @Test
    void setters_shouldUpdateValues() {
        TaskEntity task = new TaskEntity();

        task.setTitle("Nueva tarea");
        task.setDescription("Nueva descripción");
        task.setCompleted(true);

        assertEquals("Nueva tarea", task.getTitle());
        assertEquals("Nueva descripción", task.getDescription());
        assertTrue(task.isCompleted());
    }

    @Test
    void equals_shouldReturnTrueForSameValues() {
        TaskEntity task1 = new TaskEntity(1L, "Tarea", "Desc", true);
        TaskEntity task2 = new TaskEntity(1L, "Tarea", "Desc", true);

        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    void equals_shouldReturnFalseForDifferentValues() {
        TaskEntity task1 = new TaskEntity(1L, "Tarea 1", "Desc", true);
        TaskEntity task2 = new TaskEntity(2L, "Tarea 2", "Desc", true);

        assertNotEquals(task1, task2);
    }

    @Test
    void equals_shouldReturnFalseWhenComparedWithNull() {
        TaskEntity task = new TaskEntity(1L, "Tarea", "Desc", true);

        assertNotEquals(task, null);
    }

    @Test
    void equals_shouldReturnFalseWhenComparedWithDifferentClass() {
        TaskEntity task = new TaskEntity(1L, "Tarea", "Desc", true);

        assertNotEquals(task, "no es una tarea");
    }

    @Test
    void toString_shouldContainAllFields() {
        TaskEntity task = new TaskEntity(1L, "Tarea", "Descripción", true);

        String result = task.toString();

        assertTrue(result.contains("1"));
        assertTrue(result.contains("Tarea"));
        assertTrue(result.contains("Descripción"));
        assertTrue(result.contains("true"));
    }
}
