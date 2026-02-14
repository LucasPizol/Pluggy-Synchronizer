package com.application.usecase.openfinance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.domain.entities.AccountEntity;
import com.domain.entities.CashFlowEntity;
import com.domain.entities.AccountItemEntity;
import com.domain.entities.TransactionEntity;
import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.OpenFinanceTransaction;
import com.domain.repositories.transactions.ITransactionRepository;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.accountitem.IListAccountItemUseCase;
import com.domain.usecase.cashflow.IUpsertCashFlowUseCase;
import com.domain.usecase.openfinance.ITransactionSynchronizerUseCase;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TransactionsSynchronizerUseCase implements ITransactionSynchronizerUseCase {
  private static final Logger LOG = Logger.getLogger(TransactionsSynchronizerUseCase.class);
  @Inject
  private IOpenFinance openFinance;

  @Inject
  private ITransactionRepository transactionRepository;

  @Inject
  private IListAccountItemUseCase listAccountItemUseCase;

  @Inject
  private IUpsertCashFlowUseCase upsertCashFlowUseCase;

  @Override
  public void synchronizeTransactions(AccountEntity account) {
    synchronizeTransactions(account, null, null);
  }

  @Override
  public void synchronizeTransactions(AccountEntity account, LocalDate startDate) {
    synchronizeTransactions(account, startDate, null);
  }

  @Override
  @Transactional
  public void synchronizeTransactions(AccountEntity account, LocalDate startDate, String[] transactionIds) {
    LOG.infof("Account: %s", account.getId());
    CashFlowEntity cashFlow = upsertCashFlowUseCase.upsertCashFlow(account.getClientConceptId().intValue());
    List<AccountItemEntity> accountItems = listAccountItemUseCase.listAccountItems(account.getId());
    LOG.infof("Account Items count: %d", accountItems.size());

    for (AccountItemEntity accountItem : accountItems) {
      PaginatedResponse<OpenFinanceTransaction> response = openFinance.listTransactions(accountItem.getItemId(),
          startDate,
          transactionIds);

      OpenFinanceTransaction[] transactions = response.getItems();
      Set<String> existingIds = getExistingTransactionIds(transactions);

      for (OpenFinanceTransaction transaction : transactions) {
        long subcents = Math.round(transaction.getAmount() * 100);
        LocalDateTime now = LocalDateTime.now();

        if (existingIds.contains(transaction.getId())) {
          TransactionEntity existing = transactionRepository.findAllByIntegrationIds(List.of(transaction.getId()))
              .stream().findFirst().orElseThrow();
          transactionRepository.update(
              "name = ?1, originalValueSubcents = ?2, tempValueSubcents = ?3, valueSubcents = ?4, transactionDate = ?5, updatedAt = ?6 WHERE id = ?7",
              transaction.getDescription() != null ? transaction.getDescription() : "",
              subcents,
              subcents,
              subcents,
              transaction.getDate() != null ? transaction.getDate().toLocalDate() : null,
              now,
              existing.getId());
          continue;
        }

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setCashFlow(cashFlow);
        transactionEntity.setName(transaction.getDescription() != null ? transaction.getDescription() : "");
        transactionEntity.setEntryMode("debit");
        transactionEntity.setOriginalValueSubcents(subcents);
        transactionEntity.setTempValueSubcents(subcents);
        transactionEntity.setValueSubcents(subcents);
        transactionEntity
            .setTransactionDate(transaction.getDate() != null ? transaction.getDate().toLocalDate() : null);
        transactionEntity.setTransactionType(transaction.getType());
        transactionEntity.setIntegrationId(transaction.getId());
        transactionEntity.setClientConceptsCashFlowCategoryId(null);
        transactionEntity.setClientConceptsCashFlowPurchaseId(null);
        transactionEntity.setClientConceptsCashFlowSubcategoryId(null);
        transactionEntity.setCreatedAt(now);
        transactionEntity.setUpdatedAt(now);
        transactionRepository.persist(transactionEntity);
      }
    }
  }

  private Set<String> getExistingTransactionIds(OpenFinanceTransaction[] transactions) {
    List<String> transactionIdsToCheck = Arrays.stream(transactions)
        .map(OpenFinanceTransaction::getId)
        .toList();

    return transactionRepository
        .findAllByIntegrationIds(transactionIdsToCheck)
        .stream()
        .map(TransactionEntity::getIntegrationId)
        .collect(Collectors.toSet());
  }
}
