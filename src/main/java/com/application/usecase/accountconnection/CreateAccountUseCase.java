package com.application.usecase.accountconnection;

import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.OpenFinanceAccount;
import com.domain.gateway.openfinance.models.OpenFinanceAccountItem;
import com.domain.usecase.accountconnection.ICreateAccountUseCase;
import com.domain.entities.AccountEntity;
import com.domain.entities.AccountItemEntity;
import com.domain.repositories.accountitems.IAccountItemRepository;
import com.domain.repositories.accounts.IAccountRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateAccountUseCase implements ICreateAccountUseCase {
  @Inject
  private IAccountRepository accountRepository;

  @Inject
  private IOpenFinance openFinance;

  @Inject
  private IAccountItemRepository accountItemRepository;

  @Override
  public AccountEntity createAccount(String accountId) {
    OpenFinanceAccount openFinanceAccount = openFinance.getAccount(accountId);

    AccountEntity account = new AccountEntity(openFinanceAccount.getName(), openFinanceAccount.getPrimaryColor(),
        openFinanceAccount.getInstitutionLogo(), openFinanceAccount.getInstitutionId(), accountId);

    accountRepository.persist(account);

    OpenFinanceAccountItem[] openFinanceAccountItems = openFinance.listAccountItems(accountId);

    for (OpenFinanceAccountItem openFinanceAccountItem : openFinanceAccountItems) {
      AccountItemEntity accountItem = new AccountItemEntity(account, openFinanceAccountItem.getId(),
          openFinanceAccountItem.getName());
      accountItemRepository.persist(accountItem);
    }

    return account;
  }
}
