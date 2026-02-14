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

import com.domain.entities.AccountEntity;
import com.domain.entities.AccountItemEntity;
import com.domain.entities.TransactionEntity;
import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.OpenFinanceTransaction;
import com.domain.repositories.transactions.ITransactionRepository;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.accountconnection.IUpsertAccountUseCase;
import com.domain.usecase.accountconnection.IGetAccountUseCase;
import com.domain.usecase.accountitem.IListAccountItemUseCase;

class TransactionsSynchronizerUseCaseTest {

  @Mock
  private IOpenFinance openFinance;

  @Mock
  private ITransactionRepository transactionRepository;

  @Mock
  private IListAccountItemUseCase listAccountItemUseCase;

  @Mock
  private IUpsertAccountUseCase createAccountUseCase;

  @Mock
  private IGetAccountUseCase getAccountUseCase;

  @InjectMocks
  private TransactionsSynchronizerUseCase useCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldPersistNewTransactions() {
    String accountId = "account-123";
    AccountEntity account = new AccountEntity(accountId, "Test", "#000000", "https://example.com/logo.png", 1,
        accountId);
    AccountItemEntity accountItem = new AccountItemEntity(account, "item-int-1", "Conta Corrente");

    when(getAccountUseCase.getAccount(accountId)).thenReturn(account);
    when(listAccountItemUseCase.listAccountItems(account.getId())).thenReturn(List.of(accountItem));

    OpenFinanceTransaction transaction = new OpenFinanceTransaction(
        "tx-1", accountItem.getIntegrationId(), "Test transaction", 100.0,
        LocalDateTime.now(), "POSTED", "CREDIT", "Test", null);
    PaginatedResponse<OpenFinanceTransaction> response = new PaginatedResponse<>(
        1, 10, 1L, 1, new OpenFinanceTransaction[] { transaction });

    when(openFinance.listTransactions(accountItem.getIntegrationId(), null, null)).thenReturn(response);
    when(transactionRepository.findAllByIntegrationIds(anyList())).thenReturn(List.of());

    useCase.synchronizeTransactions(accountId);

    verify(transactionRepository, times(1)).persist(any(TransactionEntity.class));
    verify(transactionRepository, never()).update(anyString(), any(), any(), any(), any());
  }

  @Test
  void shouldUpdateExistingTransactions() {
    String accountId = "account-123";
    String transactionId = "tx-1";
    AccountEntity account = new AccountEntity(accountId, "Test", "#000000", "https://example.com/logo.png", 1,
        accountId);
    AccountItemEntity accountItem = new AccountItemEntity(account, "item-int-1", "Conta Corrente");
    TransactionEntity existingEntity = new TransactionEntity(
        accountItem, "Test transaction", 100.0,
        LocalDateTime.now(), "PENDING", "CREDIT", 1, null, transactionId);
    existingEntity.setId(transactionId);

    when(getAccountUseCase.getAccount(accountId)).thenReturn(account);
    when(listAccountItemUseCase.listAccountItems(account.getId())).thenReturn(List.of(accountItem));

    OpenFinanceTransaction transaction = new OpenFinanceTransaction(
        transactionId, accountItem.getIntegrationId(), "Test transaction", 150.0,
        LocalDateTime.now(), "POSTED", "CREDIT", "Test", null);
    PaginatedResponse<OpenFinanceTransaction> response = new PaginatedResponse<>(
        1, 10, 1L, 1, new OpenFinanceTransaction[] { transaction });

    when(openFinance.listTransactions(accountItem.getIntegrationId(), null, null)).thenReturn(response);
    when(transactionRepository.findAllByIntegrationIds(anyList())).thenReturn(List.of(existingEntity));

    useCase.synchronizeTransactions(accountId);

    verify(transactionRepository, never()).persist(any(TransactionEntity.class));
    verify(transactionRepository, times(1)).update(anyString(), any(), any(), any(), any());
  }

  @Test
  void shouldHandleEmptyResponse() {
    String accountId = "account-123";
    AccountEntity account = new AccountEntity(accountId, "Test", "#000000", "https://example.com/logo.png", 1,
        accountId);
    when(getAccountUseCase.getAccount(accountId)).thenReturn(account);
    when(listAccountItemUseCase.listAccountItems(account.getId())).thenReturn(List.of());

    useCase.synchronizeTransactions(accountId);

    verify(transactionRepository, never()).persist(any(TransactionEntity.class));
    verify(transactionRepository, never()).update(anyString(), any(), any(), any(), any());
  }

  @Test
  void shouldPersistOnlyNewTransactionsWhenMixed() {
    String accountId = "account-123";
    AccountEntity account = new AccountEntity(accountId, "Test", "#000000", "https://example.com/logo.png", 1,
        accountId);
    AccountItemEntity accountItem = new AccountItemEntity(account, "item-int-1", "Conta Corrente");
    TransactionEntity existingEntity = new TransactionEntity(
        accountItem, "Existing transaction", 100.0,
        LocalDateTime.now(), "PENDING", "CREDIT", 1, null, "tx-existing");
    existingEntity.setId("tx-existing");

    when(getAccountUseCase.getAccount(accountId)).thenReturn(account);
    when(listAccountItemUseCase.listAccountItems(account.getId())).thenReturn(List.of(accountItem));

    OpenFinanceTransaction newTx = new OpenFinanceTransaction(
        "tx-new", accountItem.getIntegrationId(), "New transaction", 200.0,
        LocalDateTime.now(), "POSTED", "DEBIT", "Test", null);
    OpenFinanceTransaction existingTx = new OpenFinanceTransaction(
        "tx-existing", accountItem.getIntegrationId(), "Existing transaction", 100.0,
        LocalDateTime.now(), "POSTED", "CREDIT", "Test", null);
    PaginatedResponse<OpenFinanceTransaction> response = new PaginatedResponse<>(
        1, 10, 2L, 1, new OpenFinanceTransaction[] { newTx, existingTx });

    when(openFinance.listTransactions(accountItem.getIntegrationId(), null, null)).thenReturn(response);
    when(transactionRepository.findAllByIntegrationIds(anyList())).thenReturn(List.of(existingEntity));

    useCase.synchronizeTransactions(accountId);

    verify(transactionRepository, times(1)).persist(any(TransactionEntity.class));
    verify(transactionRepository, times(1)).update(anyString(), any(), any(), any(), any());
  }
}
