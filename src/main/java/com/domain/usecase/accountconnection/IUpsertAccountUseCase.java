package com.domain.usecase.accountconnection;

import com.domain.entities.AccountEntity;

public interface IUpsertAccountUseCase {
  AccountEntity upsertAccount(Long clientConceptId, String name, String connectionId);
}
