package com.application.services.open_finance;

import java.time.LocalDate;

import org.jboss.logging.Logger;

import com.domain.gateway.open_finance.IOpenFinance;
import com.domain.gateway.open_finance.models.Transaction;
import com.domain.gateway.open_finance.models.TransactionPageResponse;
import com.domain.services.open_finance.ITransactionSynchronizer;
import com.infrastructure.persistence.entities.TransactionEntity;
import com.infrastructure.persistence.repositories.TransactionRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TransactionsSynchronizer implements ITransactionSynchronizer {
  @Inject
  private IOpenFinance openFinance;

  @Inject
  private TransactionRepository transactionRepository;

  private final Logger LOG = Logger.getLogger(TransactionsSynchronizer.class);

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
    TransactionPageResponse response = openFinance.listTransactions(accountId, startDate, transactionIds);

    LOG.infof("Total transactions found: %d", response.getTotal());

    for (Transaction transaction : response.getTransactions()) {
      TransactionEntity transactionEntity = new TransactionEntity(
          transaction.getId(),
          transaction.getAccountId(),
          transaction.getDescription(),
          transaction.getAmount(),
          transaction.getDate(),
          transaction.getStatus(),
          transaction.getType(),
          1,
          transaction.getProviderId());

      transactionRepository.persist(transactionEntity);
    }
  }
}
