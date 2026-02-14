package com.domain.usecase.openfinance;

import com.domain.entities.AccountEntity;

public interface ICategorySynchronizerUseCase {
  void synchronizeCategories(AccountEntity account);
}
