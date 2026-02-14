package com.application.usecase.openfinance;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.OpenFinanceTransaction;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.accountitem.IListAccountItemUseCase;
import com.domain.usecase.openfinance.ITransactionSynchronizerUseCase;
import com.domain.entities.AccountEntity;
import com.domain.entities.AccountItemEntity;
import com.domain.entities.TransactionEntity;
import com.domain.repositories.transactions.ITransactionRepository;

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
    List<AccountItemEntity> accountItems = listAccountItemUseCase.listAccountItems(account.getId());
    LOG.infof("Account Items count: %d", accountItems.size());

    for (AccountItemEntity accountItem : accountItems) {
      PaginatedResponse<OpenFinanceTransaction> response = openFinance.listTransactions(accountItem.getIntegrationId(),
          startDate,
          transactionIds);

      OpenFinanceTransaction[] transactions = response.getItems();
      Set<String> existingIds = getExistingTransactionIds(transactions);

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
            accountItem,
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

  private Set<String> getExistingTransactionIds(OpenFinanceTransaction[] transactions) {
    List<String> transactionIdsToCheck = Arrays.stream(transactions)
        .map(OpenFinanceTransaction::getId)
        .toList();

    Set<String> existingIds = transactionRepository
        .findAllByIntegrationIds(transactionIdsToCheck)
        .stream()
        .map(TransactionEntity::getId)
        .collect(Collectors.toSet());

    return existingIds;
  }
}
