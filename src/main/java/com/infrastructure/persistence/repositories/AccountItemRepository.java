package com.infrastructure.persistence.repositories;

import java.util.List;

import com.domain.entities.AccountItemEntity;
import com.domain.repositories.accountitems.IAccountItemRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class AccountItemRepository implements IAccountItemRepository, PanacheRepositoryBase<AccountItemEntity, Long> {

  @PersistenceContext
  EntityManager entityManager;

  @Override
  public void persist(AccountItemEntity entity) {
    entityManager.persist(entity);
  }

  @Override
  public void update(AccountItemEntity entity) {
    entityManager.merge(entity);
  }

  @Override
  public AccountItemEntity findById(Long id) {
    return entityManager.find(AccountItemEntity.class, id);
  }

  @Override
  public AccountItemEntity findByItemId(String itemId) {
    var list = entityManager
        .createQuery("SELECT a FROM AccountItemEntity a WHERE a.itemId = :itemId",
            AccountItemEntity.class)
        .setParameter("itemId", itemId)
        .setMaxResults(1)
        .getResultList();
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public List<AccountItemEntity> findByAccountId(Long accountId) {
    return entityManager
        .createQuery("SELECT a FROM AccountItemEntity a WHERE a.account.id = :accountId",
            AccountItemEntity.class)
        .setParameter("accountId", accountId)
        .getResultList();
  }

  @Override
  public long deleteAll() {
    return entityManager.createQuery("DELETE FROM AccountItemEntity").executeUpdate();
  }
}
