package com.infrastructure.persistence.repositories;

import com.domain.entities.AccountEntity;
import com.domain.repositories.accounts.IAccountRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountRepository implements IAccountRepository, PanacheRepositoryBase<AccountEntity, String> {

  @Override
  public void persist(AccountEntity entity) {
    PanacheRepositoryBase.super.persist(entity);
  }

  @Override
  public AccountEntity findById(String id) {
    return PanacheRepositoryBase.super.findById(id);
  }
}
