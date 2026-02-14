package com.application.usecase.accountconnection;

import java.time.LocalDateTime;

import com.domain.entities.AccountEntity;
import com.domain.repositories.accounts.IAccountRepository;
import com.domain.usecase.accountconnection.IGetAccountUseCase;
import com.domain.usecase.accountconnection.IUpsertAccountUseCase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpsertAccountUseCase implements IUpsertAccountUseCase {
  @Inject
  private IAccountRepository accountRepository;

  @Inject
  private IGetAccountUseCase getAccountUseCase;

  @Override
  public AccountEntity upsertAccount(Long clientConceptId, String name, String connectionId) {
    AccountEntity account = getAccountUseCase.getAccountByConnectionId(connectionId);

    if (account != null && account.getClientConceptId() != null
        && !account.getClientConceptId().equals(clientConceptId)) {
      throw new RuntimeException("Account already exists for another concept");
    }

    if (account != null) {
      account.setName(name);
      account.setUpdatedAt(LocalDateTime.now());
      if (account.getClientConceptId() == null) {
        account.setClientConceptId(clientConceptId);
      }
      accountRepository.update(account);
      return account;
    }

    account = new AccountEntity(clientConceptId, name, connectionId);
    accountRepository.persist(account);
    return account;
  }
}
