package com.application.usecase.transactions;

import com.application.dto.TransactionDTO;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.transactions.ITransactionListUseCase;
import com.domain.repositories.transactions.ITransactionRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransactionsListUseCase implements ITransactionListUseCase {
  @Inject
  private ITransactionRepository transactionRepository;

  @Override
  public PaginatedResponse<TransactionDTO> listTransactions(String accountItemId) {
    return listTransactions(accountItemId, 1);
  }

  @Override
  public PaginatedResponse<TransactionDTO> listTransactions(String accountItemId, Integer page) {
    return listTransactions(accountItemId, page, 10);
  }

  @Override
  public PaginatedResponse<TransactionDTO> listTransactions(String accountItemId, Integer page, Integer pageSize) {
    var entities = transactionRepository.findByAccountItemId(accountItemId, page, pageSize);
    long total = transactionRepository.countByAccountItemId(accountItemId);
    int totalPages = (int) Math.ceil((double) total / pageSize);

    TransactionDTO[] dtos = entities.stream()
        .map(TransactionDTO::fromEntity)
        .toArray(TransactionDTO[]::new);

    return new PaginatedResponse<>(page, pageSize, total, totalPages, dtos);
  }
}
