package com.domain.usecase.accountitem;

import com.domain.entities.AccountEntity;
import com.domain.entities.AccountItemEntity;

public interface ICreateAccountItemUseCase {
  AccountItemEntity createAccountItem(AccountEntity account, String itemId, String name);
}
