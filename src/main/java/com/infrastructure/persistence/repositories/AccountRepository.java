package com.infrastructure.persistence.repositories;

import com.domain.entities.AccountEntity;
import com.domain.repositories.accounts.IAccountRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class AccountRepository implements IAccountRepository, PanacheRepositoryBase<AccountEntity, Long> {

  @PersistenceContext
  EntityManager entityManager;

  @Override
  public void persist(AccountEntity entity) {
    entityManager.persist(entity);
  }

  @Override
  public AccountEntity findById(Long id) {
    return entityManager.find(AccountEntity.class, id);
  }

  @Override
  public AccountEntity findByConnectionId(String connectionId) {
    var list = entityManager
        .createQuery("SELECT a FROM AccountEntity a WHERE a.connectionId = :connectionId", AccountEntity.class)
        .setParameter("connectionId", connectionId)
        .setMaxResults(1)
        .getResultList();
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public void update(AccountEntity entity) {
    entityManager.merge(entity);
  }

  @Override
  public long deleteAll() {
    return entityManager.createQuery("DELETE FROM AccountEntity").executeUpdate();
  }
}
