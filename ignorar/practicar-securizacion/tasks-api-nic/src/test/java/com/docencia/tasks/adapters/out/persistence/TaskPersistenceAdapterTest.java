package com.docencia.tasks.adapters.out.persistence;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.docencia.tasks.adapters.mapper.TaskMapper;
import com.docencia.tasks.adapters.out.persistence.jpa.TaskJpaEntity;
import com.docencia.tasks.adapters.out.persistence.repository.TaskRepositoryRepository;
import com.docencia.tasks.domain.model.Task;

class TaskPersistenceAdapterTest {

    TaskRepositoryRepository jpaRepo;
    TaskMapper mapper;
    TaskPersistenceAdapter adapter;
    TaskJpaEntity taskEntity;
    Task task;

    @BeforeEach
    void setUp() {
        jpaRepo = mock(TaskRepositoryRepository.class);
        mapper = mock(TaskMapper.class);
        adapter = new TaskPersistenceAdapter(jpaRepo, mapper);
        taskEntity = new TaskJpaEntity(1L, "t", "d", false);
        task = new Task(1L, "t", "d", false);
    }

    @Test
    void findAllMapsEntitiesToDomainTest() {
        when(jpaRepo.findAll()).thenReturn(List.of(taskEntity));

        when(mapper.toDomain(taskEntity)).thenReturn(task);

        List<Task> result = adapter.findAll();
        
        assertEquals(1, result.size());
        verify(jpaRepo).findAll();
        verify(mapper).toDomain(taskEntity);
    }

    @Test
    void findByIdMapsOptionalTest() {
        when(jpaRepo.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(mapper.toDomain(taskEntity)).thenReturn(new Task(1L, "t", "d", false));

        Optional<Task> result = adapter.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("t", result.get().getTitle());
        assertEquals("d", result.get().getDescription());
        assertFalse(result.get().isCompleted());
    
        verify(jpaRepo).findById(1L);
        verify(mapper).toDomain(taskEntity);
    }

    @Test
    void existByIdTest() {
        when(jpaRepo.existsById(1L)).thenReturn(Boolean.TRUE);
        boolean result = adapter.existsById(1L);
        assertTrue(result);
    }

    @Test
    void saveTest() {
        when(mapper.toJpa(task)).thenReturn(taskEntity);
        when(jpaRepo.save(taskEntity)).thenReturn(taskEntity);
        when(mapper.toDomain(taskEntity)).thenReturn(task);
        Task result = adapter.save(task);
        assertEquals(task, result);

        verify(mapper).toJpa(task);
        verify(jpaRepo).save(taskEntity);
        verify(mapper).toDomain(taskEntity);
    }

    @Test
    void deleteByIdTest() {
        adapter.deleteById(1L);
        verify(jpaRepo).deleteById(1L);
        when(jpaRepo.existsById(1L)).thenReturn(Boolean.FALSE);
        boolean result = adapter.existsById(1L);
        assertFalse(result);
    }

}
