package com.docencia.tasks.adapters.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.mapper.TaskMapperImpl;
import com.docencia.tasks.adapters.out.persistence.jpa.TaskJpaEntity;
import com.docencia.tasks.domain.model.Task;

class TaskMapperTest {

    TaskMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new TaskMapperImpl();
    }

    @Test
    void toDomain_fromRequest_shouldMapAllFields() {
        TaskRequest request = new TaskRequest();
        request.setTitle("title");
        request.setDescription("desc");
        request.setCompleted(true);

        Task task = mapper.toDomain(request);

        assertNotNull(task);
        assertEquals("title", task.getTitle());
        assertEquals("desc", task.getDescription());
        assertTrue(task.isCompleted());
    }

    @Test
    void toDomain_shouldNotChangeCompletedWhenNull() {
        TaskRequest request = new TaskRequest();
        request.setCompleted(null); 
        Task task = mapper.toDomain(request);
        assertNotNull(task);
        assertFalse(task.isCompleted(), "El campo completed debe quedarse en false por defecto");
    }

    @Test
    void toDomain_nullRequest_shouldReturnNull() {
        assertNull(mapper.toDomain((TaskRequest) null));
    }

    @Test
    void toResponse_fromDomain_shouldMapAllFields() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("title");
        task.setDescription("desc");
        task.setCompleted(true);

        TaskResponse response = mapper.toResponse(task);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("title", response.getTitle());
        assertEquals("desc", response.getDescription());
        assertTrue(response.isCompleted());
    }

    @Test
    void toResponse_nullTask_shouldReturnNull() {
        assertNull(mapper.toResponse(null));
    }

    @Test
    void toJpa_fromDomain_shouldMapAllFields() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("title");
        task.setDescription("desc");
        task.setCompleted(true);

        TaskJpaEntity entity = mapper.toJpa(task);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("title", entity.getTitle());
        assertEquals("desc", entity.getDescription());
        assertTrue(entity.isCompleted());
    }

    @Test
    void toJpa_nullTask_shouldReturnNull() {
        assertNull(mapper.toJpa(null));
    }

    @Test
    void toDomain_fromJpa_shouldMapAllFields() {
        TaskJpaEntity entity = new TaskJpaEntity();
        entity.setId(1L);
        entity.setTitle("title");
        entity.setDescription("desc");
        entity.setCompleted(true);

        Task task = mapper.toDomain(entity);

        assertNotNull(task);
        assertEquals(1L, task.getId());
        assertEquals("title", task.getTitle());
        assertEquals("desc", task.getDescription());
        assertTrue(task.isCompleted());
    }

    @Test
    void toDomain_nullJpa_shouldReturnNull() {
        assertNull(mapper.toDomain((TaskJpaEntity) null));
    }

    @Test
    void updateDomainFromRequest_partialUpdate_shouldOnlyUpdateNonNull() {
        Task task = new Task();
        task.setTitle("oldTitle");
        task.setDescription("oldDesc");
        task.setCompleted(false);

        TaskRequest request = new TaskRequest();
        request.setTitle("newTitle");
        request.setCompleted(true);

        mapper.updateDomainFromRequest(request, task);

        assertEquals("newTitle", task.getTitle());
        assertEquals("oldDesc", task.getDescription());
        assertTrue(task.isCompleted());
    }

    @Test
    void updateDomainFromRequest_nullRequest_shouldDoNothing() {
        Task task = new Task();
        task.setTitle("oldTitle");
        task.setDescription("oldDesc");
        task.setCompleted(false);

        mapper.updateDomainFromRequest(null, task);

        assertEquals("oldTitle", task.getTitle());
        assertEquals("oldDesc", task.getDescription());
        assertFalse(task.isCompleted());
    }

    @Test
    void updateDomainFromRequest_shouldUpdateTitleOnly() {
        Task task = new Task();
        task.setTitle("Old Title");
        TaskRequest request = new TaskRequest();
        request.setTitle("New Title");

        mapper.updateDomainFromRequest(request, task);

        assertEquals("New Title", task.getTitle());
    }

    @Test
    void updateDomainFromRequest_shouldUpdateDescriptionOnly() {
        Task task = new Task();
        task.setDescription("Old Description");
        TaskRequest request = new TaskRequest();
        request.setDescription("New Description");

        mapper.updateDomainFromRequest(request, task);

        assertEquals("New Description", task.getDescription());
    }

    @Test
    void updateDomainFromRequest_shouldUpdateCompletedOnly() {
        Task task = new Task();
        task.setCompleted(false);
        TaskRequest request = new TaskRequest();
        request.setCompleted(true);

        mapper.updateDomainFromRequest(request, task);

        assertTrue(task.isCompleted());
    }

    @Test
    void updateDomainFromRequest_withAllNull_shouldNotChangeAnything() {
        Task task = new Task();
        task.setTitle("Title");
        task.setDescription("Description");
        task.setCompleted(false);

        TaskRequest request = new TaskRequest();
        mapper.updateDomainFromRequest(request, task);

        assertEquals("Title", task.getTitle());
        assertEquals("Description", task.getDescription());
        assertFalse(task.isCompleted());
    }
}
