package com.domain.repositories.accounts;

import com.domain.entities.AccountEntity;

public interface IAccountRepository {

  void persist(AccountEntity entity);

  AccountEntity findById(String id);
}
