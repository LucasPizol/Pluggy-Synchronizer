package com.application.usecase.accountitem;

import java.util.List;

import com.domain.usecase.accountitem.IListAccountItemUseCase;
import com.infrastructure.persistence.entities.AccountItemEntity;
import com.infrastructure.persistence.repositories.AccountItemRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ListAccountItemUseCase implements IListAccountItemUseCase {
  @Inject
  private AccountItemRepository accountItemRepository;

  @Override
  public List<AccountItemEntity> listAccountItems(String accountId) {
    return accountItemRepository.findByAccountId(accountId);
  }
}
