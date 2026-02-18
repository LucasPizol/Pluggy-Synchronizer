package com.application.usecase.openfinance;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.domain.entities.AccountEntity;
import com.domain.entities.AccountItemEntity;
import com.domain.entities.CashFlowEntity;
import com.domain.entities.TransactionEntity;
import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.OpenFinanceTransaction;
import com.domain.repositories.transactions.ITransactionRepository;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.accountitem.IListAccountItemUseCase;
import com.domain.usecase.cashflow.IUpsertCashFlowUseCase;
import com.domain.usecase.categories.ICategoryLookupUseCase;
import com.domain.usecase.subcategories.ISubcategoryLookupUseCase;

class TransactionsSynchronizerUseCaseTest {

  @Mock
  private IOpenFinance openFinance;

  @Mock
  private ITransactionRepository transactionRepository;

  @Mock
  private IListAccountItemUseCase listAccountItemUseCase;

  @Mock
  private IUpsertCashFlowUseCase upsertCashFlowUseCase;

  @Mock
  private ICategoryLookupUseCase categoryLookupUseCase;

  @Mock
  private ISubcategoryLookupUseCase subcategoryLookupUseCase;

  @InjectMocks
  private TransactionsSynchronizerUseCase useCase;

  private AccountEntity account;
  private AccountItemEntity accountItem;
  private CashFlowEntity cashFlow;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    account = new AccountEntity(1L, "Test Account", "conn-123");
    account.setId(1L);

    accountItem = new AccountItemEntity(account, "item-123", "Conta Corrente");

    cashFlow = new CashFlowEntity();
    cashFlow.setId(1L);

    when(upsertCashFlowUseCase.upsertCashFlow(any())).thenReturn(cashFlow);
    when(categoryLookupUseCase.findByNames(any(), any())).thenReturn(Collections.emptyList());
    when(subcategoryLookupUseCase.findByNames(any(), any())).thenReturn(Collections.emptyList());
  }

  @Test
  void shouldPersistNewTransactions() {
    when(listAccountItemUseCase.listAccountItems(account.getId())).thenReturn(List.of(accountItem));

    OpenFinanceTransaction transaction = new OpenFinanceTransaction(
        "tx-1", accountItem.getItemId(), "Test transaction", 100.0,
        LocalDateTime.now(), "POSTED", "CREDIT", "cat-1", null);
    PaginatedResponse<OpenFinanceTransaction> response = new PaginatedResponse<>(
        1, 10, 1L, 1, new OpenFinanceTransaction[] { transaction });

    when(openFinance.listTransactions(accountItem.getItemId(), null, null)).thenReturn(response);
    when(transactionRepository.findAllByIntegrationIds(anyList())).thenReturn(List.of());

    useCase.synchronizeTransactions(account);

    verify(transactionRepository, times(1)).persist(any(TransactionEntity.class));
  }

  @Test
  void shouldUpdateExistingTransactions() {
    String transactionId = "tx-1";

    TransactionEntity existingEntity = new TransactionEntity();
    existingEntity.setId(1L);
    existingEntity.setIntegrationId(transactionId);
    existingEntity.setName("Test transaction");

    when(listAccountItemUseCase.listAccountItems(account.getId())).thenReturn(List.of(accountItem));

    OpenFinanceTransaction transaction = new OpenFinanceTransaction(
        transactionId, accountItem.getItemId(), "Test transaction", 150.0,
        LocalDateTime.now(), "POSTED", "CREDIT", "cat-1", null);
    PaginatedResponse<OpenFinanceTransaction> response = new PaginatedResponse<>(
        1, 10, 1L, 1, new OpenFinanceTransaction[] { transaction });

    when(openFinance.listTransactions(accountItem.getItemId(), null, null)).thenReturn(response);
    when(transactionRepository.findAllByIntegrationIds(anyList())).thenReturn(List.of(existingEntity));

    useCase.synchronizeTransactions(account);

    verify(transactionRepository, never()).persist(any(TransactionEntity.class));
    verify(transactionRepository, times(1)).update(anyString(), any(), any(), any(), any(), any(), any(), any(), any());
  }

  @Test
  void shouldHandleEmptyResponse() {
    when(listAccountItemUseCase.listAccountItems(account.getId())).thenReturn(List.of());

    useCase.synchronizeTransactions(account);

    verify(transactionRepository, never()).persist(any(TransactionEntity.class));
  }

  @Test
  void shouldPersistOnlyNewTransactionsWhenMixed() {
    TransactionEntity existingEntity = new TransactionEntity();
    existingEntity.setId(1L);
    existingEntity.setIntegrationId("tx-existing");
    existingEntity.setName("Existing transaction");

    when(listAccountItemUseCase.listAccountItems(account.getId())).thenReturn(List.of(accountItem));

    OpenFinanceTransaction newTx = new OpenFinanceTransaction(
        "tx-new", accountItem.getItemId(), "New transaction", 200.0,
        LocalDateTime.now(), "POSTED", "DEBIT", "cat-1", null);
    OpenFinanceTransaction existingTx = new OpenFinanceTransaction(
        "tx-existing", accountItem.getItemId(), "Existing transaction", 100.0,
        LocalDateTime.now(), "POSTED", "CREDIT", "cat-1", null);
    PaginatedResponse<OpenFinanceTransaction> response = new PaginatedResponse<>(
        1, 10, 2L, 1, new OpenFinanceTransaction[] { newTx, existingTx });

    when(openFinance.listTransactions(accountItem.getItemId(), null, null)).thenReturn(response);
    when(transactionRepository.findAllByIntegrationIds(anyList())).thenReturn(List.of(existingEntity));

    useCase.synchronizeTransactions(account);

    verify(transactionRepository, times(1)).persist(any(TransactionEntity.class));
    verify(transactionRepository, times(1)).update(anyString(), any(), any(), any(), any(), any(), any(), any(), any());
  }
}
