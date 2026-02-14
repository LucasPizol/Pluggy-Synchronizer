package com.infrastructure.persistence.repositories;

import java.time.LocalDate;
import java.util.List;

import com.domain.entities.TransactionEntity;
import com.domain.repositories.transactions.ITransactionRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class TransactionRepository implements ITransactionRepository, PanacheRepositoryBase<TransactionEntity, Long> {

  @PersistenceContext
  EntityManager entityManager;

  @Override
  public void persist(TransactionEntity entity) {
    entityManager.persist(entity);
  }

  @Override
  public TransactionEntity findById(Long id) {
    return entityManager.find(TransactionEntity.class, id);
  }

  @Override
  public int update(String query, Object... params) {
    String hql = "UPDATE TransactionEntity SET " + query;
    var q = entityManager.createQuery(hql);
    for (int i = 0; i < params.length; i++) {
      q.setParameter(i + 1, params[i]);
    }
    return q.executeUpdate();
  }

  @Override
  public List<TransactionEntity> findByCashFlowId(Long cashFlowId, Integer page, Integer pageSize) {
    int first = (page - 1) * pageSize;
    return entityManager
        .createQuery(
            "SELECT t FROM TransactionEntity t WHERE t.cashFlow.id = :cashFlowId ORDER BY t.transactionDate DESC",
            TransactionEntity.class)
        .setParameter("cashFlowId", cashFlowId)
        .setFirstResult(first)
        .setMaxResults(pageSize)
        .getResultList();
  }

  public List<TransactionEntity> findByCashFlowIdAndDateAfter(Long cashFlowId, LocalDate startDate) {
    return entityManager
        .createQuery(
            "SELECT t FROM TransactionEntity t WHERE t.cashFlow.id = :cashFlowId AND t.transactionDate >= :startDate",
            TransactionEntity.class)
        .setParameter("cashFlowId", cashFlowId)
        .setParameter("startDate", startDate)
        .getResultList();
  }

  public List<TransactionEntity> findByCategoryId(Long categoryId) {
    return entityManager
        .createQuery(
            "SELECT t FROM TransactionEntity t WHERE t.clientConceptsCashFlowCategoryId = :categoryId",
            TransactionEntity.class)
        .setParameter("categoryId", categoryId)
        .getResultList();
  }

  @Override
  public long countByCashFlowId(Long cashFlowId) {
    return entityManager
        .createQuery("SELECT COUNT(t) FROM TransactionEntity t WHERE t.cashFlow.id = :cashFlowId", Long.class)
        .setParameter("cashFlowId", cashFlowId)
        .getSingleResult();
  }

  @Override
  public List<TransactionEntity> findAllByIntegrationIds(List<String> ids) {
    if (ids == null || ids.isEmpty()) {
      return List.of();
    }
    return entityManager
        .createQuery("SELECT t FROM TransactionEntity t WHERE t.integrationId IN :ids", TransactionEntity.class)
        .setParameter("ids", ids)
        .getResultList();
  }

  @Override
  public boolean existsById(Long id) {
    return findById(id) != null;
  }

  @Override
  public long deleteAll() {
    return entityManager.createQuery("DELETE FROM TransactionEntity").executeUpdate();
  }
}
