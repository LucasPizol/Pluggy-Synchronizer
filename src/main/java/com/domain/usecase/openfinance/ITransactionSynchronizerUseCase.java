package com.domain.usecase.openfinance;

import java.time.LocalDate;

import com.domain.entities.AccountEntity;

public interface ITransactionSynchronizerUseCase {
  void synchronizeTransactions(AccountEntity account);

  void synchronizeTransactions(AccountEntity account, LocalDate startDate);

  void synchronizeTransactions(AccountEntity account, LocalDate startDate, String[] transactionIds);
}
