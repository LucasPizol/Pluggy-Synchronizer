package com.infrastructure.persistence.repositories;

import java.time.LocalDateTime;
import java.util.List;

import com.infrastructure.persistence.entities.TransactionEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransactionRepository implements PanacheRepositoryBase<TransactionEntity, String> {
  public List<TransactionEntity> findByAccountId(String accountId) {
    return this.findByAccountId(accountId, 1, 10);
  }

  public List<TransactionEntity> findByAccountId(String accountId, Integer page) {
    return this.findByAccountId(accountId, page, 10);
  }

  public List<TransactionEntity> findByAccountId(String accountId, Integer page, Integer pageSize) {
    return find("accountId", accountId).page(page - 1, pageSize).list();
  }

  public List<TransactionEntity> findByAccountIdAndDateAfter(String accountId, LocalDateTime startDate) {
    return find("accountId = ?1 and date >= ?2", accountId, startDate).list();
  }

  public List<TransactionEntity> findByCategoryId(Integer categoryId) {
    return find("categoryId", categoryId).list();
  }

  public boolean existsById(String id) {
    return findById(id) != null;
  }

  public List<TransactionEntity> findAllByIntegrationIds(List<String> ids) {
    if (ids == null || ids.isEmpty()) {
      return List.of();
    }
    return list("integrationId IN ?1", ids);
  }
}
