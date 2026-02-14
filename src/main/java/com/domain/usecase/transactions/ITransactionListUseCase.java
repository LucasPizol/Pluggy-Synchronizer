package com.domain.usecase.transactions;

import com.application.dto.TransactionDTO;
import com.domain.shared.PaginatedResponse;

public interface ITransactionListUseCase {
  PaginatedResponse<TransactionDTO> listTransactions(Long conceptId);

  PaginatedResponse<TransactionDTO> listTransactions(Long conceptId, Integer page);

  PaginatedResponse<TransactionDTO> listTransactions(Long conceptId, Integer page, Integer pageSize);
}
