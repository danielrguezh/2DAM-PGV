package com.docencia.tasks.rest.controller;

import com.docencia.tasks.entities.TaskEntity;
import com.docencia.tasks.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * @Test
    void getAll_shouldReturnListOfTasks() throws Exception {
        TaskEntity task1 = new TaskEntity(1L, "Task 1", "Desc 1", false);
        TaskEntity task2 = new TaskEntity(2L, "Task 2", "Desc 2", true);

        Mockito.when(taskRepository.findAll())
                .thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].completed").value(true));
    }

    @Test
    void create_shouldSaveAndReturnTask() throws Exception {
        TaskEntity task = new TaskEntity(null, "New Task", "New Desc", false);
        TaskEntity savedTask = new TaskEntity(1L, "New Task", "New Desc", false);

        Mockito.when(taskRepository.save(Mockito.any(TaskEntity.class)))
                .thenReturn(savedTask);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Task"));
    }

    @Test
    void update_shouldUpdateTask() throws Exception {
        TaskEntity updatedTask = new TaskEntity(1L, "Updated", "Updated Desc", true);

        Mockito.when(taskRepository.save(Mockito.any(TaskEntity.class)))
                .thenReturn(updatedTask);

        mockMvc.perform(put("/api/v1/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void delete_shouldDeleteTaskById() throws Exception {
        Mockito.doNothing().when(taskRepository).deleteById(1L);

        mockMvc.perform(delete("/api/v1/tasks/{id}", 1L))
                .andExpect(status().isOk());

        Mockito.verify(taskRepository).deleteById(1L);
    }
     */
}
