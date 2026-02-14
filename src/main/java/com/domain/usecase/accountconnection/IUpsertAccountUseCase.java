package com.domain.usecase.accountconnection;

import com.domain.entities.AccountEntity;

public interface IUpsertAccountUseCase {
  AccountEntity upsertAccount(String name, String primaryColor, String institutionLogo, Integer institutionId,
      String accountId);

}
