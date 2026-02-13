package com.domain.services.transactions;

import com.domain.shared.PaginatedResponse;
import com.infrastructure.persistence.entities.TransactionEntity;

public interface ITransactionList {
  PaginatedResponse<TransactionEntity> listTransactions(String accountId);

  PaginatedResponse<TransactionEntity> listTransactions(String accountId, Integer page);

  PaginatedResponse<TransactionEntity> listTransactions(String accountId, Integer page, Integer pageSize);
}
