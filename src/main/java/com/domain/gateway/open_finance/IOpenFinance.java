package com.domain.gateway.open_finance;

import java.time.LocalDate;

import com.domain.gateway.open_finance.models.TransactionPageResponse;

public interface IOpenFinance {
  TransactionPageResponse listTransactions(String accountId);

  TransactionPageResponse listTransactions(String accountId, LocalDate startDate);

  TransactionPageResponse listTransactions(String accountId, LocalDate startDate, String[] transactionIds);
}
