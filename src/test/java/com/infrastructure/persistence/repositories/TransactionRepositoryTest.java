package com.infrastructure.persistence.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.domain.entities.AccountEntity;
import com.domain.entities.AccountItemEntity;
import com.domain.entities.CashFlowEntity;
import com.domain.entities.TransactionEntity;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
class TransactionRepositoryTest {

  @Inject
  TransactionRepository repository;

  @Inject
  AccountRepository accountRepository;

  @Inject
  AccountItemRepository accountItemRepository;

  @Inject
  CashFlowRepository cashFlowRepository;

  @BeforeEach
  @Transactional
  void setUp() {
    repository.deleteAll();
    accountItemRepository.deleteAll();
    cashFlowRepository.deleteAll();
    accountRepository.deleteAll();
  }

  @Test
  @Transactional
  void shouldPersistAndFindTransaction() {
    AccountEntity account = new AccountEntity(1L, "Test", "account-123");
    accountRepository.persist(account);
    AccountItemEntity accountItem = new AccountItemEntity(account, "int-1", "Conta Corrente");
    accountItemRepository.persist(accountItem);
    CashFlowEntity cashFlow = new CashFlowEntity(1L);
    cashFlowRepository.persist(cashFlow);

    TransactionEntity entity = TransactionRepositoryTestFixtures.createTransaction(cashFlow, "int-tx-1", 100.0);
    repository.persist(entity);
    TransactionEntity found = repository.findById(entity.getId());

    assertNotNull(found);
    assertEquals(cashFlow.getId(), found.getCashFlow().getId());
    assertEquals(10000L, found.getValueSubcents());
  }

  @Test
  @Transactional
  void shouldFindByCashFlowId() {
    AccountEntity account = new AccountEntity(1L, "Test", "account-123");
    accountRepository.persist(account);
    AccountItemEntity accountItem = new AccountItemEntity(account, "int-1", "Conta Corrente");
    accountItemRepository.persist(accountItem);
    CashFlowEntity cashFlow = new CashFlowEntity(1L);
    cashFlowRepository.persist(cashFlow);

    TransactionEntity tx1 = TransactionRepositoryTestFixtures.createTransaction(cashFlow, "int-tx-1", 100.0);
    TransactionEntity tx2 = TransactionRepositoryTestFixtures.createTransaction(cashFlow, "int-tx-2", 200.0);
    repository.persist(tx1);
    repository.persist(tx2);

    List<TransactionEntity> result = repository.findByCashFlowId(cashFlow.getId(), 1, 10);

    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(t -> t.getCashFlow().getId().equals(cashFlow.getId())));
  }

  @Test
  @Transactional
  void shouldFindByCashFlowIdWithPagination() {
    AccountEntity account = new AccountEntity(1L, "Test", "account-123");
    accountRepository.persist(account);
    AccountItemEntity accountItem = new AccountItemEntity(account, "int-1", "Conta Corrente");
    accountItemRepository.persist(accountItem);
    CashFlowEntity cashFlow = new CashFlowEntity(1L);
    cashFlowRepository.persist(cashFlow);

    for (int i = 0; i < 25; i++) {
      TransactionEntity tx = TransactionRepositoryTestFixtures.createTransaction(cashFlow, "int-tx-" + i, 100.0 + i);
      repository.persist(tx);
    }

    List<TransactionEntity> page1 = repository.findByCashFlowId(cashFlow.getId(), 1, 10);
    List<TransactionEntity> page2 = repository.findByCashFlowId(cashFlow.getId(), 2, 10);
    List<TransactionEntity> page3 = repository.findByCashFlowId(cashFlow.getId(), 3, 10);

    assertEquals(10, page1.size());
    assertEquals(10, page2.size());
    assertEquals(5, page3.size());
  }

  @Test
  @Transactional
  void shouldFindAllByIntegrationIds() {
    AccountEntity account = new AccountEntity(1L, "Test", "account-123");
    accountRepository.persist(account);
    AccountItemEntity accountItem = new AccountItemEntity(account, "int-1", "Conta Corrente");
    accountItemRepository.persist(accountItem);
    CashFlowEntity cashFlow = new CashFlowEntity(1L);
    cashFlowRepository.persist(cashFlow);

    TransactionEntity t1 = TransactionRepositoryTestFixtures.createTransaction(cashFlow, "int-tx-1", 100.0);
    TransactionEntity t2 = TransactionRepositoryTestFixtures.createTransaction(cashFlow, "int-tx-2", 200.0);
    TransactionEntity t3 = TransactionRepositoryTestFixtures.createTransaction(cashFlow, "int-tx-3", 300.0);
    repository.persist(t1);
    repository.persist(t2);
    repository.persist(t3);

    List<TransactionEntity> result = repository.findAllByIntegrationIds(List.of("int-tx-1", "int-tx-3"));

    assertEquals(2, result.size());
    assertTrue(result.stream().anyMatch(t -> "int-tx-1".equals(t.getIntegrationId())));
    assertTrue(result.stream().anyMatch(t -> "int-tx-3".equals(t.getIntegrationId())));
  }

  @Test
  @Transactional
  void shouldReturnEmptyListWhenNoIdsProvided() {
    List<TransactionEntity> result = repository.findAllByIntegrationIds(List.of());

    assertTrue(result.isEmpty());
  }

  @Test
  @Transactional
  void shouldReturnEmptyListWhenNullIdsProvided() {
    List<TransactionEntity> result = repository.findAllByIntegrationIds(null);

    assertTrue(result.isEmpty());
  }

  @Test
  @Transactional
  void shouldCheckExistsById() {
    AccountEntity account = new AccountEntity(1L, "Test", "account-123");
    accountRepository.persist(account);
    AccountItemEntity accountItem = new AccountItemEntity(account, "int-1", "Conta Corrente");
    accountItemRepository.persist(accountItem);
    CashFlowEntity cashFlow = new CashFlowEntity(1L);
    cashFlowRepository.persist(cashFlow);
    TransactionEntity tx = TransactionRepositoryTestFixtures.createTransaction(cashFlow, "int-tx-1", 100.0);
    repository.persist(tx);

    assertTrue(repository.existsById(tx.getId()));
    assertFalse(repository.existsById(99999L));
  }

  @Test
  @Transactional
  void shouldFindByCategoryId() {
    AccountEntity account = new AccountEntity(1L, "Test", "account-123");
    accountRepository.persist(account);
    AccountItemEntity accountItem = new AccountItemEntity(account, "int-1", "Conta Corrente");
    accountItemRepository.persist(accountItem);
    CashFlowEntity cashFlow = new CashFlowEntity(1L);
    cashFlowRepository.persist(cashFlow);

    TransactionEntity entity1 = TransactionRepositoryTestFixtures.createTransaction(cashFlow, "int-1", 100.0);
    entity1.setClientConceptsCashFlowCategoryId(1L);
    TransactionEntity entity2 = TransactionRepositoryTestFixtures.createTransaction(cashFlow, "int-2", 200.0);
    entity2.setClientConceptsCashFlowCategoryId(2L);
    TransactionEntity entity3 = TransactionRepositoryTestFixtures.createTransaction(cashFlow, "int-3", 300.0);
    entity3.setClientConceptsCashFlowCategoryId(1L);

    repository.persist(entity1);
    repository.persist(entity2);
    repository.persist(entity3);

    List<TransactionEntity> result = repository.findByCategoryId(1L);

    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(t -> Long.valueOf(1L).equals(t.getClientConceptsCashFlowCategoryId())));
  }
}
