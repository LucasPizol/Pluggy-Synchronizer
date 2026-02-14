package com.application.usecase.transactions;

import com.application.dto.TransactionDTO;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.transactions.ITransactionListUseCase;
import com.domain.repositories.transactions.ITransactionRepository;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransactionsListUseCase implements ITransactionListUseCase {
  private static final Logger LOG = Logger.getLogger(TransactionsListUseCase.class);
  @Inject
  private ITransactionRepository transactionRepository;

  @Override
  public PaginatedResponse<TransactionDTO> listTransactions(Long cashFlowId) {
    return listTransactions(cashFlowId, 1);
  }

  @Override
  public PaginatedResponse<TransactionDTO> listTransactions(Long cashFlowId, Integer page) {
    return listTransactions(cashFlowId, page, 10);
  }

  @Override
  public PaginatedResponse<TransactionDTO> listTransactions(Long cashFlowId, Integer page, Integer pageSize) {
    LOG.infof("Listing transactions for cash flow: %s", cashFlowId);
    var entities = transactionRepository.findByCashFlowId(cashFlowId, page, pageSize);
    long total = transactionRepository.countByCashFlowId(cashFlowId);
    int totalPages = (int) Math.ceil((double) total / pageSize);

    TransactionDTO[] dtos = entities.stream()
        .map(TransactionDTO::fromEntity)
        .toArray(TransactionDTO[]::new);

    return new PaginatedResponse<>(page, pageSize, total, totalPages, dtos);
  }
}
