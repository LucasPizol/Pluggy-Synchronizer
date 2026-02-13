package com.domain.usecase.accountitem;

import java.util.List;

import com.infrastructure.persistence.entities.AccountItemEntity;

public interface IListAccountItemUseCase {
  List<AccountItemEntity> listAccountItems(String accountId);
}
