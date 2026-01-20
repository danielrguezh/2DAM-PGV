package com.docencia.tasks.adapters.out.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class TaskJpaEntityTest {

    private static final String TITULO = "Soy un titulo";
    private static final long ID_INICIAL = 1l;
    private static final String DESCRIPCION = "Descripcion";
    TaskJpaEntity jpaEntity = null;

    @BeforeEach
    void setUp(){
        jpaEntity = new TaskJpaEntity();
    }

    @Test
    @Order(1)
    void jpaEntityNotNullTest(){
        Assertions.assertNotNull(jpaEntity, "El valor no puede ser nulo");
    }

    @Test
    @Order(2)
    void jpaEntityGetsSetsTest(){
        jpaEntity.setCompleted(true);
        jpaEntity.setDescription(DESCRIPCION);
        jpaEntity.setId(ID_INICIAL);
        jpaEntity.setTitle(TITULO);

        Assertions.assertTrue(jpaEntity.isCompleted());
        Assertions.assertEquals(DESCRIPCION, jpaEntity.getDescription());
        Assertions.assertEquals(ID_INICIAL, jpaEntity.getId());
        Assertions.assertEquals(TITULO, jpaEntity.getTitle());
    }

    @Test
    void constructorWithId_shouldSetId() {
        Long expectedId = 42L;
        TaskJpaEntity entity = new TaskJpaEntity(expectedId);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(expectedId);
    }

    @Test
    void defaultConstructor_shouldCreateObject() {
        TaskJpaEntity entity = new TaskJpaEntity();
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull(); 
    }

    
    @Test
    void equals_shouldReturnTrueForSameObject() {
        TaskJpaEntity entity = new TaskJpaEntity(1L);
        assertThat(entity.equals(entity)).isTrue(); // mismo objeto
    }

    @Test
    void equals_shouldReturnTrueForSameId() {
        TaskJpaEntity entity1 = new TaskJpaEntity(1L);
        TaskJpaEntity entity2 = new TaskJpaEntity(1L);

        assertThat(entity1.equals(entity2)).isTrue();
        assertThat(entity2.equals(entity1)).isTrue();
    }

    @Test
    void equals_shouldReturnFalseForDifferentId() {
        TaskJpaEntity entity1 = new TaskJpaEntity(1L);
        TaskJpaEntity entity2 = new TaskJpaEntity(2L);

        assertThat(entity1.equals(entity2)).isFalse();
        assertThat(entity2.equals(entity1)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        TaskJpaEntity entity = new TaskJpaEntity(1L);
        assertThat(entity.equals(null)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseForDifferentClass() {
        TaskJpaEntity entity = new TaskJpaEntity(1L);
        String other = "not a TaskJpaEntity";

        assertThat(entity.equals(other)).isFalse();
    }

    @Test
    void hashCode_shouldBeSameForSameId() {
        TaskJpaEntity entity1 = new TaskJpaEntity(1L);
        TaskJpaEntity entity2 = new TaskJpaEntity(1L);

        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    void hashCode_shouldBeDifferentForDifferentId() {
        TaskJpaEntity entity1 = new TaskJpaEntity(1L);
        TaskJpaEntity entity2 = new TaskJpaEntity(2L);

        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());
    }
}
