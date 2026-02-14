package com.application.usecase.cashflow;

import com.domain.entities.CashFlowEntity;
import com.domain.repositories.cashflow.ICashFlowRepository;
import com.domain.usecase.cashflow.IGetCashFlowByConceptUseCase;
import com.domain.usecase.cashflow.IUpsertCashFlowUseCase;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UpsertCashFlowUseCase implements IUpsertCashFlowUseCase {
  @Inject
  private IGetCashFlowByConceptUseCase getCashFlowByConceptUseCase;

  @Inject
  private ICashFlowRepository cashFlowRepository;

  @Override
  public CashFlowEntity upsertCashFlow(Long conceptId) {
    CashFlowEntity cashFlow = getCashFlowByConceptUseCase.getCashFlowByConcept(conceptId);

    if (cashFlow != null) {
      return cashFlow;
    }

    cashFlow = new CashFlowEntity(conceptId);
    cashFlowRepository.persist(cashFlow);
    return cashFlow;
  }
}
