package com.application.usecase.accountconnection;

import com.domain.usecase.accountconnection.IUpsertAccountUseCase;
import com.domain.usecase.accountconnection.IGetAccountUseCase;
import com.domain.entities.AccountEntity;
import com.domain.repositories.accounts.IAccountRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpsertAccountUseCase implements IUpsertAccountUseCase {
  @Inject
  private IAccountRepository accountRepository;

  @Inject
  private IGetAccountUseCase getAccountUseCase;

  @Override
  public AccountEntity upsertAccount(String name, String primaryColor, String institutionLogo, Integer institutionId,
      String accountId) {
    AccountEntity account = getAccountUseCase.getAccount(accountId);

    if (account != null) {
      return account;
    }

    account = new AccountEntity(name, primaryColor, institutionLogo, institutionId, accountId);
    accountRepository.persist(account);
    return account;
  }
}
