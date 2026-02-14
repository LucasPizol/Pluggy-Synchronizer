package com.domain.repositories.transactions;

import java.util.List;

import com.domain.entities.TransactionEntity;

public interface ITransactionRepository {

  void persist(TransactionEntity entity);

  TransactionEntity findById(String id);

  boolean existsById(String id);

  long countByAccountItemId(String accountItemId);

  List<TransactionEntity> findByAccountItemId(String accountItemId, Integer page, Integer pageSize);

  List<TransactionEntity> findAllByIntegrationIds(List<String> integrationIds);

  int update(String query, Object... params);
}
