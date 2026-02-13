package com.domain.usecase.openfinance;

import java.time.LocalDate;

public interface ITransactionSynchronizerUseCase {
  void synchronizeTransactions(String accountId);

  void synchronizeTransactions(String accountId, LocalDate startDate);

  void synchronizeTransactions(String accountId, LocalDate startDate, String[] transactionIds);
}
