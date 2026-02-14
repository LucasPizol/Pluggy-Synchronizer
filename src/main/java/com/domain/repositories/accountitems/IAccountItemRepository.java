package com.domain.repositories.accountitems;

import java.util.List;

import com.domain.entities.AccountItemEntity;

public interface IAccountItemRepository {

  void persist(AccountItemEntity entity);

  void update(AccountItemEntity entity);

  AccountItemEntity findByItemId(String itemId);

  AccountItemEntity findById(Long id);

  List<AccountItemEntity> findByAccountId(Long accountId);
}
