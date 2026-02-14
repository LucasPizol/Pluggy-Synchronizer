package com.infrastructure.persistence.repositories;

import java.time.LocalDateTime;
import java.util.List;

import com.domain.entities.TransactionEntity;
import com.domain.repositories.transactions.ITransactionRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class TransactionRepository implements ITransactionRepository, PanacheRepositoryBase<TransactionEntity, String> {

  @PersistenceContext
  EntityManager entityManager;

  @Override
  public void persist(TransactionEntity entity) {
    entityManager.persist(entity);
  }

  @Override
  public TransactionEntity findById(String id) {
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
  public List<TransactionEntity> findByAccountItemId(String accountItemId, Integer page, Integer pageSize) {
    return find("accountItem.id", accountItemId).page(page - 1, pageSize).list();
  }

  public List<TransactionEntity> findByAccountItemIdAndDateAfter(String accountItemId, LocalDateTime startDate) {
    return find("accountItem.id = ?1 and date >= ?2", accountItemId, startDate).list();
  }

  public List<TransactionEntity> findByCategoryId(Integer categoryId) {
    return find("categoryId", categoryId).list();
  }

  @Override
  public long countByAccountItemId(String accountItemId) {
    return count("accountItem.id", accountItemId);
  }

  @Override
  public List<TransactionEntity> findAllByIntegrationIds(List<String> ids) {
    if (ids == null || ids.isEmpty()) {
      return List.of();
    }
    return list("integrationId IN ?1", ids);
  }

  @Override
  public boolean existsById(String id) {
    return findById(id) != null;
  }
}
