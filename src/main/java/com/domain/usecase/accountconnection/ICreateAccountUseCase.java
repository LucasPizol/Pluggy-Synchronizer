package com.domain.usecase.accountconnection;

import com.domain.entities.AccountEntity;

public interface ICreateAccountUseCase {
  AccountEntity createAccount(String accountId);

}
