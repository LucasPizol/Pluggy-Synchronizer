package com.application.usecase.cashflow;

import com.domain.entities.CashFlowEntity;
import com.domain.repositories.cashflow.ICashFlowRepository;
import com.domain.usecase.cashflow.IGetCashFlowByConceptUseCase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetCashFlowByConceptUseCase implements IGetCashFlowByConceptUseCase {
  @Inject
  private ICashFlowRepository cashFlowRepository;

  @Override
  public CashFlowEntity getCashFlowByConcept(Integer conceptId) {
    return cashFlowRepository.findByClientConceptId(conceptId.longValue());
  }
}
