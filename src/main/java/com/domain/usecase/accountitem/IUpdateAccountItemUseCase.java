package com.domain.usecase.accountitem;

import com.domain.entities.AccountItemEntity;

public interface IUpdateAccountItemUseCase {
  AccountItemEntity updateAccountItem(Long accountItemId, String name);

}
