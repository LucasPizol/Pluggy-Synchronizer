package com.application.usecase.openfinance;

import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.OpenFinanceAccount;
import com.domain.usecase.accountconnection.IUpsertAccountUseCase;
import com.domain.usecase.openfinance.IAccountItemSynchronizerUseCase;
import com.domain.usecase.openfinance.IAccountSynchronizerUseCase;
import com.domain.usecase.openfinance.ITransactionSynchronizerUseCase;
import com.domain.entities.AccountEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AccountSynchronizerUseCase implements IAccountSynchronizerUseCase {
  @Inject
  private IUpsertAccountUseCase upsertAccountUseCase;

  @Inject
  private IAccountItemSynchronizerUseCase accountItemSynchronizerUseCase;

  @Inject
  private ITransactionSynchronizerUseCase transactionSynchronizerUseCase;

  @Inject
  private IOpenFinance openFinance;

  @Override
  public AccountEntity synchronizeAccount(String accountId) {
    OpenFinanceAccount openFinanceAccount = openFinance.getAccount(accountId);

    AccountEntity account = upsertAccountUseCase.upsertAccount(
        openFinanceAccount.getName(),
        openFinanceAccount.getPrimaryColor(),
        openFinanceAccount.getInstitutionLogo(),
        openFinanceAccount.getInstitutionId(),
        accountId);

    accountItemSynchronizerUseCase.synchronizeAccountItems(account);
    transactionSynchronizerUseCase.synchronizeTransactions(account);

    return account;
  }
}
