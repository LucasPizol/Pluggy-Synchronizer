package com.domain.usecase.cashflow;

import com.domain.entities.CashFlowEntity;

public interface IUpsertCashFlowUseCase {
  CashFlowEntity upsertCashFlow(Integer conceptId);

}
