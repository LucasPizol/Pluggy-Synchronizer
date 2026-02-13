package com.domain.usecase.accountitem;

import com.infrastructure.persistence.entities.AccountEntity;
import com.infrastructure.persistence.entities.AccountItemEntity;

public interface ICreateAccountItemUseCase {
  AccountItemEntity createAccountItem(AccountEntity account, String integrationId, String name);
}
