package com.domain.repositories.accountitems;

import java.util.List;

import com.domain.entities.AccountItemEntity;

public interface IAccountItemRepository {

  void persist(AccountItemEntity entity);

  AccountItemEntity findByIntegrationId(String integrationId);

  List<AccountItemEntity> findByAccountId(String accountId);

  AccountItemEntity findById(String id);
}
