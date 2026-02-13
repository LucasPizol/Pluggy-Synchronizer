package com.infrastructure.persistence.repositories;

import java.util.List;

import com.infrastructure.persistence.entities.AccountItemEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountItemRepository implements PanacheRepositoryBase<AccountItemEntity, String> {

  public AccountItemEntity findByIntegrationId(String integrationId) {
    return find("integrationId", integrationId).firstResult();
  }

  public List<AccountItemEntity> findByAccountId(String accountId) {
    return find("account.id", accountId).list();
  }
}
