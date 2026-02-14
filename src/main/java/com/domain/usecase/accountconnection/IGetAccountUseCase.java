package com.domain.usecase.accountconnection;

import com.domain.entities.AccountEntity;

public interface IGetAccountUseCase {
  AccountEntity getAccountByConnectionId(String connectionId);
}
