package com.resources;

import com.domain.usecase.transactions.ITransactionListUseCase;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;

@Path("/transactions")
public class Transactions {
  @Inject
  private ITransactionListUseCase transactionsListUseCase;

  @GET
  public Response listTransactions(@QueryParam("cashFlowId") Long cashFlowId, @QueryParam("page") Integer page,
      @QueryParam("pageSize") Integer pageSize) {
    return Response.ok(transactionsListUseCase.listTransactions(cashFlowId, page, pageSize)).build();
  }
}
