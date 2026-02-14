package com.infrastructure.persistence.repositories;

import java.time.LocalDateTime;

import com.domain.entities.AccountItemEntity;
import com.domain.entities.TransactionEntity;

/**
 * Fixtures para TransactionRepositoryTest. Métodos que retornam entidades Panache
 * ficam fora da classe de teste para não interferir no bytecode enhancement do Quarkus.
 */
public final class TransactionRepositoryTestFixtures {

  private TransactionRepositoryTestFixtures() {
  }

  public static TransactionEntity createTransaction(AccountItemEntity accountItem, String integrationId, double amount) {
    return new TransactionEntity(
        accountItem, "Test transaction", amount,
        LocalDateTime.now(), "POSTED", "CREDIT", 1, null, integrationId);
  }

  public static TransactionEntity createTransaction(AccountItemEntity accountItem, String id, String integrationId,
      double amount) {
    TransactionEntity tx = new TransactionEntity(
        accountItem, "Test transaction", amount,
        LocalDateTime.now(), "POSTED", "CREDIT", 1, null, integrationId);
    tx.setId(id);
    return tx;
  }
}
