package com.infrastructure.persistence.repositories;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.domain.entities.SubcategoryEntity;
import com.domain.repositories.subcategories.ISubcategoryRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class SubcategoryRepository implements ISubcategoryRepository, PanacheRepositoryBase<SubcategoryEntity, Long> {

  @PersistenceContext
  EntityManager entityManager;

  @Override
  public void persist(SubcategoryEntity entity) {
    entityManager.persist(entity);
  }

  @Override
  public void update(SubcategoryEntity entity) {
    entityManager.merge(entity);
  }

  @Override
  public SubcategoryEntity findByClientConceptsCashFlowIdAndPluggyId(Long clientConceptsCashFlowId, String pluggyId) {
    if (pluggyId == null || pluggyId.isBlank()) {
      return null;
    }
    var list = entityManager
        .createQuery(
            "SELECT s FROM SubcategoryEntity s WHERE s.clientConceptsCashFlowId = :cashFlowId AND s.pluggyId = :pluggyId",
            SubcategoryEntity.class)
        .setParameter("cashFlowId", clientConceptsCashFlowId)
        .setParameter("pluggyId", pluggyId)
        .setMaxResults(1)
        .getResultList();
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public List<SubcategoryEntity> findByCategoryId(Long categoryId) {
    return entityManager
        .createQuery("SELECT s FROM SubcategoryEntity s WHERE s.category.id = :categoryId ORDER BY s.name",
            SubcategoryEntity.class)
        .setParameter("categoryId", categoryId)
        .getResultList();
  }

  @Override
  public List<SubcategoryEntity> findByCategoryIds(Collection<Long> categoryIds) {
    if (categoryIds == null || categoryIds.isEmpty()) {
      return Collections.emptyList();
    }
    return entityManager
        .createQuery("SELECT s FROM SubcategoryEntity s WHERE s.category.id IN :categoryIds ORDER BY s.name",
            SubcategoryEntity.class)
        .setParameter("categoryIds", categoryIds)
        .getResultList();
  }

  @Override
  public List<SubcategoryEntity> findByClientConceptsCashFlowId(Long clientConceptsCashFlowId) {
    return entityManager
        .createQuery("SELECT s FROM SubcategoryEntity s WHERE s.clientConceptsCashFlowId = :cashFlowId ORDER BY s.name",
            SubcategoryEntity.class)
        .setParameter("cashFlowId", clientConceptsCashFlowId)
        .getResultList();
  }

  @Override
  public List<SubcategoryEntity> findByClientConceptsCashFlowIdAndPluggyIds(Long clientConceptsCashFlowId,
      Collection<String> pluggyIds) {
    if (pluggyIds == null || pluggyIds.isEmpty()) {
      return Collections.emptyList();
    }
    return entityManager
        .createQuery(
            "SELECT s FROM SubcategoryEntity s WHERE s.clientConceptsCashFlowId = :cashFlowId AND s.pluggyId IN :pluggyIds",
            SubcategoryEntity.class)
        .setParameter("cashFlowId", clientConceptsCashFlowId)
        .setParameter("pluggyIds", pluggyIds)
        .getResultList();
  }

  @Override
  public List<SubcategoryEntity> findByClientConceptsCashFlowIdAndNames(Long clientConceptsCashFlowId,
      Collection<String> names) {
    if (names == null || names.isEmpty()) {
      return Collections.emptyList();
    }
    return entityManager
        .createQuery(
            "SELECT s FROM SubcategoryEntity s WHERE s.clientConceptsCashFlowId = :cashFlowId AND s.name IN :names",
            SubcategoryEntity.class)
        .setParameter("cashFlowId", clientConceptsCashFlowId)
        .setParameter("names", names)
        .getResultList();
  }
}
