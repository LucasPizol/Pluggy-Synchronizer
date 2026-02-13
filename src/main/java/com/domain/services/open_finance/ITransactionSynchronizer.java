package com.domain.services.open_finance;

import java.time.LocalDate;

public interface ITransactionSynchronizer {
  void synchronizeTransactions(String accountId);

  void synchronizeTransactions(String accountId, LocalDate startDate);

  void synchronizeTransactions(String accountId, LocalDate startDate, String[] transactionIds);
}
