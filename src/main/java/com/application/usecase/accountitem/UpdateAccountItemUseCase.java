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
  public AccountItemEntity updateAccountItem(Long accountItemId, String name) {
    AccountItemEntity accountItem = accountItemRepository.findById(accountItemId);
    accountItem.setName(name);
    accountItem.setUpdatedAt(java.time.LocalDateTime.now());
    accountItemRepository.update(accountItem);
    return accountItem;
  }
}
