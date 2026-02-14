package com.application.usecase.accountitem;

import com.domain.usecase.accountitem.ICreateAccountItemUseCase;
import com.domain.entities.AccountItemEntity;
import com.domain.entities.AccountEntity;
import com.domain.repositories.accountitems.IAccountItemRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateAccountItemUseCase implements ICreateAccountItemUseCase {
  @Inject
  private IAccountItemRepository accountItemRepository;

  @Override
  public AccountItemEntity createAccountItem(AccountEntity account, String integrationId, String name) {
    AccountItemEntity accountItem = new AccountItemEntity(
        account, integrationId, name);

    accountItemRepository.persist(accountItem);
    return accountItem;
  }
}
