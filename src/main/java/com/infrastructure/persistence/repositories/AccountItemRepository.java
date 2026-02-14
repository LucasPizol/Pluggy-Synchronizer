package com.infrastructure.persistence.repositories;

import java.util.List;

import com.domain.entities.AccountItemEntity;
import com.domain.repositories.accountitems.IAccountItemRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountItemRepository implements IAccountItemRepository, PanacheRepositoryBase<AccountItemEntity, String> {

  @Override
  public void persist(AccountItemEntity entity) {
    PanacheRepositoryBase.super.persist(entity);
  }

  @Override
  public AccountItemEntity findByIntegrationId(String integrationId) {
    return find("integrationId", integrationId).firstResult();
  }

  @Override
  public List<AccountItemEntity> findByAccountId(String accountId) {
    return find("account.id", accountId).list();
  }
}
