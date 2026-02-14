package com.infrastructure.persistence.repositories;

import com.domain.entities.CashFlowEntity;
import com.domain.repositories.cashflow.ICashFlowRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class CashFlowRepository implements ICashFlowRepository, PanacheRepositoryBase<CashFlowEntity, Long> {

  @PersistenceContext
  EntityManager entityManager;

  @Override
  public void persist(CashFlowEntity entity) {
    entityManager.persist(entity);
  }

  @Override
  public CashFlowEntity findByClientConceptId(Long clientConceptId) {
    var list = entityManager
        .createQuery("SELECT c FROM CashFlowEntity c WHERE c.clientConceptId = :clientConceptId",
            CashFlowEntity.class)
        .setParameter("clientConceptId", clientConceptId)
        .setMaxResults(1)
        .getResultList();
    return list.isEmpty() ? null : list.get(0);
  }

  @Override
  public CashFlowEntity create(CashFlowEntity cashFlow) {
    entityManager.persist(cashFlow);
    return cashFlow;
  }

  @Override
  public long deleteAll() {
    return entityManager.createQuery("DELETE FROM CashFlowEntity").executeUpdate();
  }
}
