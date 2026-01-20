package com.docencia.tasks.adapters.in.controller;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.mapper.TaskMapper;
import com.docencia.tasks.business.interfaces.ITaskService;
import com.docencia.tasks.domain.model.Task;

class TaskControllerTest {
  Task task = null;
  ITaskService service;
  TaskMapper mapper;
  TaskController controller;
  
  @BeforeEach
  void setUp() {
      task = new Task(1L, "a", "b", false);
  
      service = mock(ITaskService.class);
      mapper = mock(TaskMapper.class);
      controller = new TaskController(service, mapper);
  }

  @Test
  void getAll_returnsMappedResponses() {
    ITaskService service = mock(ITaskService.class);
    TaskMapper mapper = mock(TaskMapper.class);
    TaskController controller = new TaskController(service, mapper);

    when(service.getAll()).thenReturn(List.of(task));
    when(mapper.toResponse(task)).thenReturn(new TaskResponse(1L, "a", "b", false));

    List<TaskResponse> res = controller.getAll();

    assertEquals(1, res.size());
    assertEquals(1L, res.get(0).getId());
    verify(service).getAll();
    verify(mapper).toResponse(task);
  }

  @Test
  void getById_returns404_whenNotFound() {
    ITaskService service = mock(ITaskService.class);
    TaskMapper mapper = mock(TaskMapper.class);
    TaskController controller = new TaskController(service, mapper);

    when(service.getById(10L)).thenReturn(Optional.empty());

    var resp = controller.getById(10L);

    assertEquals(404, resp.getStatusCode().value());
  }

  @Test
  void create_returns201_andBody() {
    ITaskService service = mock(ITaskService.class);
    TaskMapper mapper = mock(TaskMapper.class);
    TaskController controller = new TaskController(service, mapper);

    TaskRequest req = new TaskRequest();
    req.setTitle("t");
    req.setDescription("d");
    req.setCompleted(false);

    Task domain = new Task(null, "t", "d", false);
    Task saved = new Task(1L, "t", "d", false);

    when(mapper.toDomain(req)).thenReturn(domain);
    when(service.create(domain)).thenReturn(saved);
    when(mapper.toResponse(saved)).thenReturn(new TaskResponse(1L, "t", "d", false));

    var resp = controller.create(req);

    assertEquals(201, resp.getStatusCode().value());
    assertNotNull(resp.getBody());
    assertEquals(1L, resp.getBody().getId());
  }

  @Test
  void deleteTaskNotExistTest() {
    ITaskService service = mock(ITaskService.class);
    TaskMapper mapper = mock(TaskMapper.class);
    TaskController controller = new TaskController(service, mapper);
    ResponseEntity<Void> respuesta = controller.delete(1l);
    respuesta.getBody();
  }

  @Test
  void deleteTaskExistTest() {
    ITaskService service = mock(ITaskService.class);
    TaskMapper mapper = mock(TaskMapper.class);
    when(service.delete(anyLong())).thenReturn(true);
    TaskController controller = new TaskController(service, mapper);
    ResponseEntity<Void> respuesta = controller.delete(1l);
  }

  @Test
    void update_existingTask_shouldReturnOk() {
        Long taskId = 1L;
        TaskRequest request = new TaskRequest();
        request.setTitle("New Title");
        request.setDescription("New Desc");
        request.setCompleted(true);

        Task patch = new Task();
        patch.setTitle(request.getTitle());
        patch.setDescription(request.getDescription());
        patch.setCompleted(true);

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("New Title");
        updatedTask.setDescription("New Desc");
        updatedTask.setCompleted(true);

        TaskResponse response = new TaskResponse();
        response.setId(taskId);
        response.setTitle("New Title");
        response.setDescription("New Desc");
        response.setCompleted(true);

        when(service.update(taskId, patch)).thenReturn(Optional.of(updatedTask));
        when(mapper.toResponse(updatedTask)).thenReturn(response);

        ResponseEntity<TaskResponse> result = controller.update(taskId, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);

        verify(service).update(taskId, patch);
        verify(mapper).toResponse(updatedTask);
    }

    @Test
    void update_nonExistingTask_shouldReturnNotFound() {
        Long taskId = 1L;
        TaskRequest request = new TaskRequest();
        request.setTitle("New Title");

        Task patch = new Task();
        patch.setTitle(request.getTitle());
        patch.setCompleted(false); 

        when(service.update(taskId, patch)).thenReturn(Optional.empty());

        ResponseEntity<TaskResponse> result = controller.update(taskId, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isNull();

        verify(service).update(taskId, patch);
        verifyNoInteractions(mapper);
    }

}
