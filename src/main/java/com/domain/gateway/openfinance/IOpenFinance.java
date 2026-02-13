package com.domain.gateway.openfinance;

import java.time.LocalDate;

import com.domain.gateway.openfinance.models.OpenFinanceAccount;
import com.domain.gateway.openfinance.models.OpenFinanceAccountItem;
import com.domain.gateway.openfinance.models.OpenFinanceTransaction;
import com.domain.shared.PaginatedResponse;

public interface IOpenFinance {
  PaginatedResponse<OpenFinanceTransaction> listTransactions(String accountId);

  PaginatedResponse<OpenFinanceTransaction> listTransactions(String accountId, LocalDate startDate);

  PaginatedResponse<OpenFinanceTransaction> listTransactions(String accountId, LocalDate startDate,
      String[] transactionIds);

  OpenFinanceAccount getAccount(String accountId);

  OpenFinanceAccountItem[] listAccountItems(String accountId);

}
