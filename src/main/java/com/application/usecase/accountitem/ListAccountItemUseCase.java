package com.application.usecase.accountitem;

import java.util.List;

import com.domain.usecase.accountitem.IListAccountItemUseCase;
import com.domain.entities.AccountItemEntity;
import com.domain.repositories.accountitems.IAccountItemRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ListAccountItemUseCase implements IListAccountItemUseCase {
  @Inject
  private IAccountItemRepository accountItemRepository;

  @Override
  public List<AccountItemEntity> listAccountItems(String accountId) {
    return accountItemRepository.findByAccountId(accountId);
  }
}
