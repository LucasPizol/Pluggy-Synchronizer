package com.domain.usecase.open_finance;

import java.time.LocalDate;

public interface ITransactionSynchronizerUseCase {
  void synchronizeTransactions(String accountId);

  void synchronizeTransactions(String accountId, LocalDate startDate);

  void synchronizeTransactions(String accountId, LocalDate startDate, String[] transactionIds);
}
