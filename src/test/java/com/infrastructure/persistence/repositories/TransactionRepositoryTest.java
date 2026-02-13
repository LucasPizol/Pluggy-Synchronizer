package com.infrastructure.persistence.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.infrastructure.persistence.entities.AccountEntity;
import com.infrastructure.persistence.entities.TransactionEntity;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
class TransactionRepositoryTest {

  @Inject
  TransactionRepository repository;

  @BeforeEach
  @Transactional
  void setUp() {
    repository.deleteAll();
  }

  @Test
  @Transactional
  void shouldPersistAndFindTransaction() {
    AccountEntity account = new AccountEntity("account-123", "Test", "#000000", "https://example.com/logo.png", 1,
        "account-123");
    TransactionEntity entity = createTransaction("tx-1", account, 100.0);

    repository.persist(entity);
    TransactionEntity found = repository.findById("tx-1");

    assertNotNull(found);
    assertEquals("tx-1", found.getId());
    assertEquals("account-123", found.getAccount().getId());
    assertEquals(100.0, found.getAmount());
  }

  @Test
  @Transactional
  void shouldFindByAccountId() {
    AccountEntity account = new AccountEntity("account-123", "Test", "#000000", "https://example.com/logo.png", 1,
        "account-123");
    repository.persist(createTransaction("tx-1", account, 100.0));
    repository.persist(createTransaction("tx-2", account, 200.0));
    repository.persist(createTransaction("tx-3", account, 300.0));

    List<TransactionEntity> result = repository.findByAccountId("account-123");

    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(t -> t.getAccount().getId().equals("account-123")));
  }

  @Test
  @Transactional
  void shouldFindByAccountIdWithPagination() {
    for (int i = 0; i < 25; i++) {
      AccountEntity account = new AccountEntity("account-123", "Test", "#000000", "https://example.com/logo.png", 1,
          "account-123");
      repository.persist(createTransaction("tx-" + i, account, 100.0 + i));
    }

    List<TransactionEntity> page1 = repository.findByAccountId("account-123", 1, 10);
    List<TransactionEntity> page2 = repository.findByAccountId("account-123", 2, 10);
    List<TransactionEntity> page3 = repository.findByAccountId("account-123", 3, 10);

    assertEquals(10, page1.size());
    assertEquals(10, page2.size());
    assertEquals(5, page3.size());
  }

  @Test
  @Transactional
  void shouldFindAllByIds() {
    AccountEntity account = new AccountEntity("account-123", "Test", "#000000", "https://example.com/logo.png", 1,
        "account-123");
    repository.persist(createTransaction("tx-1", account, 100.0));
    repository.persist(createTransaction("tx-2", account, 200.0));
    repository.persist(createTransaction("tx-3", account, 300.0));

    List<TransactionEntity> result = repository.findAllByIntegrationIds(List.of("tx-1", "tx-3"));

    assertEquals(2, result.size());
    assertTrue(result.stream().anyMatch(t -> t.getId().equals("tx-1")));
    assertTrue(result.stream().anyMatch(t -> t.getId().equals("tx-3")));
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
    repository.persist(createTransaction("tx-1", account, 100.0));

    assertTrue(repository.existsById("tx-1"));
    assertFalse(repository.existsById("tx-nonexistent"));
  }

  @Test
  @Transactional
  void shouldFindByCategoryId() {
    AccountEntity account = new AccountEntity("account-123", "Test", "#000000", "https://example.com/logo.png", 1,
        "account-123");

    TransactionEntity entity1 = createTransaction("tx-1", account, 100.0);
    entity1.setCategoryId(1);
    TransactionEntity entity2 = createTransaction("tx-2", account, 200.0);
    entity2.setCategoryId(2);
    TransactionEntity entity3 = createTransaction("tx-3", account, 300.0);
    entity3.setCategoryId(1);

    repository.persist(entity1);
    repository.persist(entity2);
    repository.persist(entity3);

    List<TransactionEntity> result = repository.findByCategoryId(1);

    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(t -> t.getCategoryId().equals(1)));
  }

  private TransactionEntity createTransaction(String id, AccountEntity account, double amount) {
    return new TransactionEntity(
        account, "Test transaction", amount,
        LocalDateTime.now(), "POSTED", "CREDIT", 1, null, id);
  }
}
