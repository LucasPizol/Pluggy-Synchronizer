package com.domain.usecase.cashflow;

import com.domain.entities.CashFlowEntity;

public interface IGetCashFlowByConceptUseCase {
  CashFlowEntity getCashFlowByConcept(Integer conceptId);

}
