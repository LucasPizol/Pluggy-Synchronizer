package com.domain.usecase.transactions;

import com.application.dto.TransactionDTO;
import com.domain.shared.PaginatedResponse;

public interface ITransactionListUseCase {
  PaginatedResponse<TransactionDTO> listTransactions(String accountItemId);

  PaginatedResponse<TransactionDTO> listTransactions(String accountItemId, Integer page);

  PaginatedResponse<TransactionDTO> listTransactions(String accountItemId, Integer page, Integer pageSize);
}
