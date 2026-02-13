package com.application.usecase.accountconnection;

import com.domain.usecase.accountconnection.IGetAccountUseCase;
import com.infrastructure.persistence.entities.AccountEntity;
import com.infrastructure.persistence.repositories.AccountRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetAccountUseCase implements IGetAccountUseCase {
  @Inject
  private AccountRepository accountRepository;

  @Override
  public AccountEntity getAccount(String accountId) {
    return accountRepository.findById(accountId);
  }
}
