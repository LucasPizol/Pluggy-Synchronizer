package com.domain.usecase.openfinance;

import com.domain.entities.AccountEntity;

public interface IAccountItemSynchronizerUseCase {
  void synchronizeAccountItems(AccountEntity account);

}
