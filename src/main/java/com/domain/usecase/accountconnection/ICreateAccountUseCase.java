package com.domain.usecase.accountconnection;

import com.infrastructure.persistence.entities.AccountEntity;

public interface ICreateAccountUseCase {
  AccountEntity createAccount(String accountId);

}
