package com.application.usecase.accountitem;

import com.domain.usecase.accountitem.ICreateAccountItemUseCase;
import com.infrastructure.persistence.entities.AccountItemEntity;
import com.infrastructure.persistence.repositories.AccountItemRepository;
import com.infrastructure.persistence.entities.AccountEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateAccountItemUseCase implements ICreateAccountItemUseCase {
  @Inject
  private AccountItemRepository accountItemRepository;

  @Override
  public AccountItemEntity createAccountItem(AccountEntity account, String integrationId, String name) {
    AccountItemEntity accountItem = new AccountItemEntity(
        account, integrationId, name);

    accountItemRepository.persist(accountItem);
    return accountItem;
  }
}
