package com.application.usecase.accountconnection;

import com.domain.usecase.accountconnection.IGetAccountUseCase;
import com.domain.entities.AccountEntity;
import com.domain.repositories.accounts.IAccountRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetAccountUseCase implements IGetAccountUseCase {
  @Inject
  private IAccountRepository accountRepository;

  @Override
  public AccountEntity getAccount(String accountId) {
    return accountRepository.findById(accountId);
  }
}
