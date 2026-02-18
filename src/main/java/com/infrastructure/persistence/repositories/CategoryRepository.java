package com.infrastructure.persistence.repositories;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.domain.entities.CategoryEntity;
import com.domain.repositories.categories.ICategoryRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class CategoryRepository implements ICategoryRepository, PanacheRepositoryBase<CategoryEntity, Long> {

  @PersistenceContext
  EntityManager entityManager;

  @Override
  public void persist(CategoryEntity entity) {
    entityManager.persist(entity);
  }

  @Override
  public CategoryEntity findByClientConceptsCashFlowIdAndName(Long clientConceptsCashFlowId, String name) {
    var list = entityManager
        .createQuery(
            "SELECT c FROM CategoryEntity c WHERE c.clientConceptsCashFlowId = :cashFlowId AND c.name = :name",
            CategoryEntity.class)
        .setParameter("cashFlowId", clientConceptsCashFlowId)
        .setParameter("name", name)
        .setMaxResults(1)
        .getResultList();
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public CategoryEntity findByClientConceptsCashFlowIdAndPluggyId(Long clientConceptsCashFlowId, String pluggyId) {
    if (pluggyId == null || pluggyId.isBlank()) {
      return null;
    }
    var list = entityManager
        .createQuery(
            "SELECT c FROM CategoryEntity c WHERE c.clientConceptsCashFlowId = :cashFlowId AND c.pluggyId = :pluggyId",
            CategoryEntity.class)
        .setParameter("cashFlowId", clientConceptsCashFlowId)
        .setParameter("pluggyId", pluggyId)
        .setMaxResults(1)
        .getResultList();
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public List<CategoryEntity> findByClientConceptsCashFlowIdAndPluggyIds(Long clientConceptsCashFlowId,
      Collection<String> pluggyIds) {
    if (pluggyIds == null || pluggyIds.isEmpty()) {
      return Collections.emptyList();
    }
    return entityManager
        .createQuery(
            "SELECT c FROM CategoryEntity c WHERE c.clientConceptsCashFlowId = :cashFlowId AND c.pluggyId IN :pluggyIds",
            CategoryEntity.class)
        .setParameter("cashFlowId", clientConceptsCashFlowId)
        .setParameter("pluggyIds", pluggyIds)
        .getResultList();
  }

  @Override
  public List<CategoryEntity> findByClientConceptsCashFlowIdAndNames(Long clientConceptsCashFlowId,
      Collection<String> names) {
    if (names == null || names.isEmpty()) {
      return Collections.emptyList();
    }
    return entityManager
        .createQuery(
            "SELECT c FROM CategoryEntity c WHERE c.clientConceptsCashFlowId = :cashFlowId AND c.originalName IN :names",
            CategoryEntity.class)
        .setParameter("cashFlowId", clientConceptsCashFlowId)
        .setParameter("names", names)
        .getResultList();
  }

  @Override
  public List<CategoryEntity> findByIds(Collection<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return Collections.emptyList();
    }
    return entityManager
        .createQuery("SELECT c FROM CategoryEntity c WHERE c.id IN :ids", CategoryEntity.class)
        .setParameter("ids", ids)
        .getResultList();
  }

  @Override
  public List<CategoryEntity> findByClientConceptsCashFlowId(Long clientConceptsCashFlowId) {
    return entityManager
        .createQuery("SELECT c FROM CategoryEntity c WHERE c.clientConceptsCashFlowId = :cashFlowId",
            CategoryEntity.class)
        .setParameter("cashFlowId", clientConceptsCashFlowId)
        .getResultList();
  }

  @Override
  public List<CategoryEntity> findByClientConceptsCashFlowId(Long clientConceptsCashFlowId,
      Integer page, Integer pageSize) {
    int first = (page - 1) * pageSize;
    return entityManager
        .createQuery(
            "SELECT DISTINCT c FROM CategoryEntity c LEFT JOIN FETCH c.subcategories WHERE c.clientConceptsCashFlowId = :cashFlowId ORDER BY c.name",
            CategoryEntity.class)
        .setParameter("cashFlowId", clientConceptsCashFlowId)
        .setFirstResult(first)
        .setMaxResults(pageSize)
        .getResultList();
  }

  @Override
  public long countByClientConceptsCashFlowId(Long clientConceptsCashFlowId) {
    return entityManager
        .createQuery("SELECT COUNT(c) FROM CategoryEntity c WHERE c.clientConceptsCashFlowId = :cashFlowId",
            Long.class)
        .setParameter("cashFlowId", clientConceptsCashFlowId)
        .getSingleResult();
  }
}
