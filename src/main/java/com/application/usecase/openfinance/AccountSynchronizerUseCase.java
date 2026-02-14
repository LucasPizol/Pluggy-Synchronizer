package com.application.usecase.openfinance;

import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.OpenFinanceAccount;
import com.domain.usecase.accountconnection.IUpsertAccountUseCase;
import com.domain.usecase.cashflow.IUpsertCashFlowUseCase;
import com.domain.usecase.openfinance.IAccountItemSynchronizerUseCase;
import com.domain.usecase.openfinance.IAccountSynchronizerUseCase;
import com.domain.usecase.openfinance.ITransactionSynchronizerUseCase;
import com.domain.entities.AccountEntity;
import com.domain.entities.CashFlowEntity;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AccountSynchronizerUseCase implements IAccountSynchronizerUseCase {
  private static final Logger LOG = Logger.getLogger(AccountSynchronizerUseCase.class);

  @Inject
  private IUpsertAccountUseCase upsertAccountUseCase;

  @Inject
  private IUpsertCashFlowUseCase upsertCashFlowUseCase;

  @Inject
  private IAccountItemSynchronizerUseCase accountItemSynchronizerUseCase;

  @Inject
  private ITransactionSynchronizerUseCase transactionSynchronizerUseCase;

  @Inject
  private IOpenFinance openFinance;

  @Override
  public AccountEntity synchronizeAccount(Integer conceptId, Long cashFlowId, String connectionId) {
    CashFlowEntity cashFlow = upsertCashFlowUseCase.upsertCashFlow(conceptId);

    LOG.infof("ConnectionId: %s", connectionId);

    OpenFinanceAccount openFinanceAccount = openFinance.getAccount(connectionId);

    LOG.infof("OpenFinanceAccount: %s", openFinanceAccount.getName());

    AccountEntity account = upsertAccountUseCase.upsertAccount(
        cashFlow.getClientConceptId(),
        openFinanceAccount.getName(),
        connectionId);

    LOG.infof("Synchronizing account items and transactions");

    accountItemSynchronizerUseCase.synchronizeAccountItems(account);

    LOG.infof("Synchronizing transactions");

    transactionSynchronizerUseCase.synchronizeTransactions(account);

    return account;
  }
}
