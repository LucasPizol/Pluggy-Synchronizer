package com.application.usecase.accountitem;

import com.domain.entities.AccountItemEntity;
import com.domain.repositories.accountitems.IAccountItemRepository;
import com.domain.usecase.accountitem.IUpdateAccountItemUseCase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdateAccountItemUseCase implements IUpdateAccountItemUseCase {

  @Inject
  private IAccountItemRepository accountItemRepository;

  @Override
  public AccountItemEntity updateAccountItem(String accountItemId, String name) {
    AccountItemEntity accountItem = accountItemRepository.findById(accountItemId);
    accountItem.setName(name);
    accountItemRepository.persist(accountItem);
    return accountItem;
  }
}
