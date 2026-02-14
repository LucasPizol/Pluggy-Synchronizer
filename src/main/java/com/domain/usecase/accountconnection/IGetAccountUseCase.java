package com.domain.usecase.accountconnection;

import com.domain.entities.AccountEntity;

public interface IGetAccountUseCase {

  AccountEntity getAccount(String accountId);
}
