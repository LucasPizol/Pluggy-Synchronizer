package com.domain.repositories.transactions;

import java.util.List;

import com.domain.entities.TransactionEntity;

public interface ITransactionRepository {

  void persist(TransactionEntity entity);

  TransactionEntity findById(Long id);

  boolean existsById(Long id);

  long countByCashFlowId(Long cashFlowId);

  List<TransactionEntity> findByCashFlowId(Long cashFlowId, Integer page, Integer pageSize);

  List<TransactionEntity> findAllByIntegrationIds(List<String> integrationIds);

  int update(String query, Object... params);
}
