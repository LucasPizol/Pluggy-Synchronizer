package com.infrastructure.persistence.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.domain.entities.CashFlowEntity;
import com.domain.entities.TransactionEntity;

/**
 * Fixtures para TransactionRepositoryTest.
 */
public final class TransactionRepositoryTestFixtures {

  private TransactionRepositoryTestFixtures() {
  }

  public static TransactionEntity createTransaction(CashFlowEntity cashFlow, String integrationId, double amount) {
    long subcents = Math.round(amount * 100);
    LocalDateTime now = LocalDateTime.now();
    TransactionEntity tx = new TransactionEntity();
    tx.setCashFlow(cashFlow);
    tx.setName("Test transaction");
    tx.setEntryMode("CREDIT");
    tx.setOriginalValueSubcents(subcents);
    tx.setTempValueSubcents(subcents);
    tx.setValueSubcents(subcents);
    tx.setTransactionDate(LocalDate.now());
    tx.setTransactionType("CREDIT");
    tx.setIntegrationId(integrationId);
    tx.setCreatedAt(now);
    tx.setUpdatedAt(now);
    return tx;
  }

  public static TransactionEntity createTransaction(CashFlowEntity cashFlow, Long id, String integrationId,
      double amount) {
    TransactionEntity tx = createTransaction(cashFlow, integrationId, amount);
    tx.setId(id);
    return tx;
  }
}
