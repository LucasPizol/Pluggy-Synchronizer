package com.domain.usecase.openfinance;

import com.domain.entities.AccountEntity;

public interface IAccountSynchronizerUseCase {
  AccountEntity synchronizeAccount(Integer conceptId, Long cashFlowId, String accountId);

}
