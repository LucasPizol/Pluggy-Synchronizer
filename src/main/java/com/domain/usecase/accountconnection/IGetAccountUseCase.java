package com.domain.usecase.accountconnection;

import com.infrastructure.persistence.entities.AccountEntity;

public interface IGetAccountUseCase {

  AccountEntity getAccount(String accountId);
}
