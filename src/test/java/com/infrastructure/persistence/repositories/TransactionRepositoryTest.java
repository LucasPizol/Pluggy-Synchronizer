package com.infrastructure.persistence.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.domain.entities.AccountEntity;
import com.domain.entities.AccountItemEntity;
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

  @BeforeEach
  @Transactional
  void setUp() {
    repository.deleteAll();
    accountItemRepository.deleteAll();
    accountRepository.deleteAll();
  }

  @Test
  @Transactional
  void shouldPersistAndFindTransaction() {
    AccountEntity account = new AccountEntity("account-123", "Test", "#000000", "https://example.com/logo.png", 1,
        "account-123");
    accountRepository.persist(account);
    AccountItemEntity accountItem = new AccountItemEntity(account, "int-1", "Conta Corrente");
    accountItemRepository.persist(accountItem);

    TransactionEntity entity = TransactionRepositoryTestFixtures.createTransaction(accountItem, "tx-1", "int-tx-1", 100.0);
    repository.persist(entity);
    TransactionEntity found = repository.findById(entity.getId());

    assertNotNull(found);
    assertEquals(accountItem.getId(), found.getAccountItem().getId());
    assertEquals(100.0, found.getAmount());
  }

  @Test
  @Transactional
  void shouldFindByAccountItemId() {
    AccountEntity account = new AccountEntity("account-123", "Test", "#000000", "https://example.com/logo.png", 1,
        "account-123");
    accountRepository.persist(account);
    AccountItemEntity accountItem = new AccountItemEntity(account, "int-1", "Conta Corrente");
    accountItemRepository.persist(accountItem);

    repository.persist(TransactionRepositoryTestFixtures.createTransaction(accountItem, "id-1", "int-tx-1", 100.0));
    repository.persist(TransactionRepositoryTestFixtures.createTransaction(accountItem, "id-2", "int-tx-2", 200.0));

    List<TransactionEntity> result = repository.findByAccountItemId(accountItem.getId(), 1, 10);

    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(t -> t.getAccountItem().getId().equals(accountItem.getId())));
  }

  @Test
  @Transactional
  void shouldFindByAccountItemIdWithPagination() {
    AccountEntity account = new AccountEntity("account-123", "Test", "#000000", "https://example.com/logo.png", 1,
        "account-123");
    accountRepository.persist(account);
    AccountItemEntity accountItem = new AccountItemEntity(account, "int-1", "Conta Corrente");
    accountItemRepository.persist(accountItem);

    for (int i = 0; i < 25; i++) {
      repository.persist(TransactionRepositoryTestFixtures.createTransaction(accountItem, "id-" + i, "int-tx-" + i, 100.0 + i));
    }

    List<TransactionEntity> page1 = repository.findByAccountItemId(accountItem.getId(), 1, 10);
    List<TransactionEntity> page2 = repository.findByAccountItemId(accountItem.getId(), 2, 10);
    List<TransactionEntity> page3 = repository.findByAccountItemId(accountItem.getId(), 3, 10);

    assertEquals(10, page1.size());
    assertEquals(10, page2.size());
    assertEquals(5, page3.size());
  }

  @Test
  @Transactional
  void shouldFindAllByIntegrationIds() {
    AccountEntity account = new AccountEntity("account-123", "Test", "#000000", "https://example.com/logo.png", 1,
        "account-123");
    accountRepository.persist(account);
    AccountItemEntity accountItem = new AccountItemEntity(account, "int-1", "Conta Corrente");
    accountItemRepository.persist(accountItem);

    repository.persist(TransactionRepositoryTestFixtures.createTransaction(accountItem, "id-1", "int-tx-1", 100.0));
    repository.persist(TransactionRepositoryTestFixtures.createTransaction(accountItem, "id-2", "int-tx-2", 200.0));
    repository.persist(TransactionRepositoryTestFixtures.createTransaction(accountItem, "id-3", "int-tx-3", 300.0));

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
    AccountEntity account = new AccountEntity("account-123", "Test", "#000000", "https://example.com/logo.png", 1,
        "account-123");
    accountRepository.persist(account);
    AccountItemEntity accountItem = new AccountItemEntity(account, "int-1", "Conta Corrente");
    accountItemRepository.persist(accountItem);
    TransactionEntity tx = TransactionRepositoryTestFixtures.createTransaction(accountItem, "tx-1", "int-tx-1", 100.0);
    repository.persist(tx);

    assertTrue(repository.existsById(tx.getId()));
    assertFalse(repository.existsById("nonexistent"));
  }

  @Test
  @Transactional
  void shouldFindByCategoryId() {
    AccountEntity account = new AccountEntity("account-123", "Test", "#000000", "https://example.com/logo.png", 1,
        "account-123");
    accountRepository.persist(account);
    AccountItemEntity accountItem = new AccountItemEntity(account, "int-1", "Conta Corrente");
    accountItemRepository.persist(accountItem);

    TransactionEntity entity1 = TransactionRepositoryTestFixtures.createTransaction(accountItem, "tx-1", "int-1", 100.0);
    entity1.setCategoryId(1);
    TransactionEntity entity2 = TransactionRepositoryTestFixtures.createTransaction(accountItem, "tx-2", "int-2", 200.0);
    entity2.setCategoryId(2);
    TransactionEntity entity3 = TransactionRepositoryTestFixtures.createTransaction(accountItem, "tx-3", "int-3", 300.0);
    entity3.setCategoryId(1);

    repository.persist(entity1);
    repository.persist(entity2);
    repository.persist(entity3);

    List<TransactionEntity> result = repository.findByCategoryId(1);

    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(t -> t.getCategoryId().equals(1)));
  }

}
