package com.infrastructure.persistence.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    TransactionEntity entity = createTransaction("tx-1", "account-123", 100.0);

    repository.persist(entity);
    TransactionEntity found = repository.findById("tx-1");

    assertNotNull(found);
    assertEquals("tx-1", found.getId());
    assertEquals("account-123", found.getAccountId());
    assertEquals(100.0, found.getAmount());
  }

  @Test
  @Transactional
  void shouldFindByAccountId() {
    repository.persist(createTransaction("tx-1", "account-123", 100.0));
    repository.persist(createTransaction("tx-2", "account-123", 200.0));
    repository.persist(createTransaction("tx-3", "account-456", 300.0));

    List<TransactionEntity> result = repository.findByAccountId("account-123");

    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(t -> t.getAccountId().equals("account-123")));
  }

  @Test
  @Transactional
  void shouldFindByAccountIdWithPagination() {
    for (int i = 0; i < 25; i++) {
      repository.persist(createTransaction("tx-" + i, "account-123", 100.0 + i));
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
    repository.persist(createTransaction("tx-1", "account-123", 100.0));
    repository.persist(createTransaction("tx-2", "account-123", 200.0));
    repository.persist(createTransaction("tx-3", "account-123", 300.0));

    List<TransactionEntity> result = repository.findAllByIds(List.of("tx-1", "tx-3"));

    assertEquals(2, result.size());
    assertTrue(result.stream().anyMatch(t -> t.getId().equals("tx-1")));
    assertTrue(result.stream().anyMatch(t -> t.getId().equals("tx-3")));
  }

  @Test
  @Transactional
  void shouldReturnEmptyListWhenNoIdsProvided() {
    List<TransactionEntity> result = repository.findAllByIds(List.of());

    assertTrue(result.isEmpty());
  }

  @Test
  @Transactional
  void shouldReturnEmptyListWhenNullIdsProvided() {
    List<TransactionEntity> result = repository.findAllByIds(null);

    assertTrue(result.isEmpty());
  }

  @Test
  @Transactional
  void shouldCheckExistsById() {
    repository.persist(createTransaction("tx-1", "account-123", 100.0));

    assertTrue(repository.existsById("tx-1"));
    assertFalse(repository.existsById("tx-nonexistent"));
  }

  @Test
  @Transactional
  void shouldFindByCategoryId() {
    TransactionEntity entity1 = createTransaction("tx-1", "account-123", 100.0);
    entity1.setCategoryId(1);
    TransactionEntity entity2 = createTransaction("tx-2", "account-123", 200.0);
    entity2.setCategoryId(2);
    TransactionEntity entity3 = createTransaction("tx-3", "account-123", 300.0);
    entity3.setCategoryId(1);

    repository.persist(entity1);
    repository.persist(entity2);
    repository.persist(entity3);

    List<TransactionEntity> result = repository.findByCategoryId(1);

    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(t -> t.getCategoryId().equals(1)));
  }

  private TransactionEntity createTransaction(String id, String accountId, double amount) {
    return new TransactionEntity(
        id, accountId, "Test transaction", amount,
        LocalDateTime.now(), "POSTED", "CREDIT", 1, null);
  }
}
