package com.domain.usecase.openfinance;

import com.domain.entities.AccountEntity;

public interface IAccountSynchronizerUseCase {
  AccountEntity synchronizeAccount(String accountId);

}
