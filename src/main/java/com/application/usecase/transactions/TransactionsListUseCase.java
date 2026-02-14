package com.application.usecase.transactions;

import com.application.dto.TransactionDTO;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.cashflow.IGetCashFlowByConceptUseCase;
import com.domain.usecase.transactions.ITransactionListUseCase;
import com.domain.entities.CashFlowEntity;
import com.domain.repositories.transactions.ITransactionRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransactionsListUseCase implements ITransactionListUseCase {
  @Inject
  private ITransactionRepository transactionRepository;

  @Inject
  private IGetCashFlowByConceptUseCase getCashFlowByConceptUseCase;

  @Override
  public PaginatedResponse<TransactionDTO> listTransactions(Long conceptId) {
    return listTransactions(conceptId, 1);
  }

  @Override
  public PaginatedResponse<TransactionDTO> listTransactions(Long conceptId, Integer page) {
    return listTransactions(conceptId, page, 10);
  }

  @Override
  public PaginatedResponse<TransactionDTO> listTransactions(Long conceptId, Integer page, Integer pageSize) {
    CashFlowEntity cashFlow = getCashFlowByConceptUseCase.getCashFlowByConcept(conceptId);

    if (cashFlow == null) {
      return new PaginatedResponse<>(page, pageSize, 0, 0, new TransactionDTO[0]);
    }

    var entities = transactionRepository.findByCashFlowId(cashFlow.getId(), page, pageSize);
    long total = transactionRepository.countByCashFlowId(cashFlow.getId());
    int totalPages = (int) Math.ceil((double) total / pageSize);

    TransactionDTO[] dtos = entities.stream()
        .map(TransactionDTO::fromEntity)
        .toArray(TransactionDTO[]::new);

    return new PaginatedResponse<>(page, pageSize, total, totalPages, dtos);
  }
}
