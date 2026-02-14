package com.domain.repositories.cashflow;

import com.domain.entities.CashFlowEntity;

public interface ICashFlowRepository {
  void persist(CashFlowEntity entity);

  CashFlowEntity findByClientConceptId(Long clientConceptId);

  CashFlowEntity create(CashFlowEntity cashFlow);
}
