package com.domain.gateway.open_finance;

import java.time.LocalDate;

import com.domain.gateway.open_finance.models.Transaction;
import com.domain.shared.PaginatedResponse;

public interface IOpenFinance {
  PaginatedResponse<Transaction> listTransactions(String accountId);

  PaginatedResponse<Transaction> listTransactions(String accountId, LocalDate startDate);

  PaginatedResponse<Transaction> listTransactions(String accountId, LocalDate startDate, String[] transactionIds);
}
