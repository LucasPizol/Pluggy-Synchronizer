package com.application.usecase.transactions;

import com.domain.shared.PaginatedResponse;
import com.domain.usecase.transactions.ITransactionListUseCase;
import com.infrastructure.persistence.entities.TransactionEntity;
import com.infrastructure.persistence.repositories.TransactionRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransactionsListUseCase implements ITransactionListUseCase {
  @Inject
  private TransactionRepository transactionRepository;

  @Override
  public PaginatedResponse<TransactionEntity> listTransactions(String accountId) {
    return listTransactions(accountId, 1);
  }

  @Override
  public PaginatedResponse<TransactionEntity> listTransactions(String accountId, Integer page) {
    return listTransactions(accountId, page, 10);
  }

  @Override
  public PaginatedResponse<TransactionEntity> listTransactions(String accountId, Integer page, Integer pageSize) {
    return new PaginatedResponse<TransactionEntity>(page, pageSize,
        transactionRepository.findByAccountId(accountId, page, pageSize).size(), 1,
        transactionRepository.findByAccountId(accountId, page, pageSize).toArray(new TransactionEntity[0]));
  }
}
