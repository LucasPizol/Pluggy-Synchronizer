package com.application.usecase.openfinance;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.OpenFinanceTransaction;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.openfinance.ITransactionSynchronizerUseCase;
import com.infrastructure.persistence.entities.TransactionEntity;
import com.infrastructure.persistence.repositories.TransactionRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TransactionsSynchronizerUseCase implements ITransactionSynchronizerUseCase {
  @Inject
  private IOpenFinance openFinance;

  @Inject
  private TransactionRepository transactionRepository;

  @Override
  public void synchronizeTransactions(String accountId) {
    synchronizeTransactions(accountId, null, null);
  }

  @Override
  public void synchronizeTransactions(String accountId, LocalDate startDate) {
    synchronizeTransactions(accountId, startDate, null);
  }

  @Override
  @Transactional
  public void synchronizeTransactions(String accountId, LocalDate startDate, String[] transactionIds) {
    PaginatedResponse<OpenFinanceTransaction> response = openFinance.listTransactions(accountId, startDate,
        transactionIds);

    OpenFinanceTransaction[] transactions = response.getItems();

    List<String> transactionIdsToCheck = Arrays.stream(transactions)
        .map(OpenFinanceTransaction::getId)
        .toList();

    Set<String> existingIds = transactionRepository
        .findAllByIntegrationIds(transactionIdsToCheck)
        .stream()
        .map(TransactionEntity::getId)
        .collect(Collectors.toSet());

    for (OpenFinanceTransaction transaction : transactions) {
      if (existingIds.contains(transaction.getId())) {
        transactionRepository.update(
            "amount = ?1, status = ?2, date = ?3 WHERE id = ?4",
            transaction.getAmount(),
            transaction.getStatus(),
            transaction.getDate(),
            transaction.getId());
        continue;
      }

      TransactionEntity transactionEntity = new TransactionEntity(
          transaction.getAccountId(),
          transaction.getDescription(),
          transaction.getAmount(),
          transaction.getDate(),
          transaction.getStatus(),
          transaction.getType(),
          1,
          transaction.getProviderId(),
          transaction.getId());

      transactionRepository.persist(transactionEntity);
    }
  }
}
