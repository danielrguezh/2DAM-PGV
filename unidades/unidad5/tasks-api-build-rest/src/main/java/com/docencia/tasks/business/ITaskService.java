package com.docencia.tasks.business;

import java.util.List;

import com.docencia.tasks.domain.model.Task;

public interface ITaskService {
    public Task createTask(Task task);
    public List<Task> getTasks();
    public Task getTaskById(Long id);
    public Task updateTask(Task task);
    public boolean deleteTask(Task task);
}
