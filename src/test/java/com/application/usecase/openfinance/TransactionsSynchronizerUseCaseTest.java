package com.application.usecase.openfinance;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.Transaction;
import com.domain.shared.PaginatedResponse;
import com.infrastructure.persistence.entities.TransactionEntity;
import com.infrastructure.persistence.repositories.TransactionRepository;

class TransactionsSynchronizerUseCaseTest {

  @Mock
  private IOpenFinance openFinance;

  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private TransactionsSynchronizerUseCase useCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldPersistNewTransactions() {
    String accountId = "account-123";
    Transaction transaction = new Transaction(
        "tx-1", accountId, "Test transaction", 100.0,
        LocalDateTime.now(), "POSTED", "CREDIT", "Test", null);

    PaginatedResponse<Transaction> response = new PaginatedResponse<>(
        1, 10, 1L, 1, new Transaction[] { transaction });

    when(openFinance.listTransactions(accountId, null, null)).thenReturn(response);
    when(transactionRepository.findAllByIds(anyList())).thenReturn(List.of());

    useCase.synchronizeTransactions(accountId);

    verify(transactionRepository, times(1)).persist(any(TransactionEntity.class));
    verify(transactionRepository, never()).update(anyString(), any(), any(), any(), any());
  }

  @Test
  void shouldUpdateExistingTransactions() {
    String accountId = "account-123";
    String transactionId = "tx-1";
    Transaction transaction = new Transaction(
        transactionId, accountId, "Test transaction", 150.0,
        LocalDateTime.now(), "POSTED", "CREDIT", "Test", null);

    PaginatedResponse<Transaction> response = new PaginatedResponse<>(
        1, 10, 1L, 1, new Transaction[] { transaction });

    TransactionEntity existingEntity = new TransactionEntity(
        transactionId, accountId, "Test transaction", 100.0,
        LocalDateTime.now(), "PENDING", "CREDIT", 1, null);

    when(openFinance.listTransactions(accountId, null, null)).thenReturn(response);
    when(transactionRepository.findAllByIds(anyList())).thenReturn(List.of(existingEntity));

    useCase.synchronizeTransactions(accountId);

    verify(transactionRepository, never()).persist(any(TransactionEntity.class));
    verify(transactionRepository, times(1)).update(anyString(), any(), any(), any(), any());
  }

  @Test
  void shouldHandleEmptyResponse() {
    String accountId = "account-123";
    PaginatedResponse<Transaction> response = new PaginatedResponse<>(
        1, 10, 0L, 0, new Transaction[] {});

    when(openFinance.listTransactions(accountId, null, null)).thenReturn(response);

    useCase.synchronizeTransactions(accountId);

    verify(transactionRepository, never()).persist(any(TransactionEntity.class));
    verify(transactionRepository, never()).update(anyString(), any(), any(), any(), any());
  }

  @Test
  void shouldPersistOnlyNewTransactionsWhenMixed() {
    String accountId = "account-123";
    Transaction newTransaction = new Transaction(
        "tx-new", accountId, "New transaction", 200.0,
        LocalDateTime.now(), "POSTED", "DEBIT", "Test", null);
    Transaction existingTransaction = new Transaction(
        "tx-existing", accountId, "Existing transaction", 100.0,
        LocalDateTime.now(), "POSTED", "CREDIT", "Test", null);

    PaginatedResponse<Transaction> response = new PaginatedResponse<>(
        1, 10, 2L, 1, new Transaction[] { newTransaction, existingTransaction });

    TransactionEntity existingEntity = new TransactionEntity(
        "tx-existing", accountId, "Existing transaction", 100.0,
        LocalDateTime.now(), "PENDING", "CREDIT", 1, null);

    when(openFinance.listTransactions(accountId, null, null)).thenReturn(response);
    when(transactionRepository.findAllByIds(anyList())).thenReturn(List.of(existingEntity));

    useCase.synchronizeTransactions(accountId);

    verify(transactionRepository, times(1)).persist(any(TransactionEntity.class));
    verify(transactionRepository, times(1)).update(anyString(), any(), any(), any(), any());
  }
}
