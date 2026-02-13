package com.resources;

import com.domain.usecase.open_finance.ITransactionSynchronizerUseCase;
import com.domain.usecase.transactions.ITransactionListUseCase;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionsSynchronizer {
  @Inject
  private ITransactionSynchronizerUseCase transactionSynchronizer;

  @Inject
  private ITransactionListUseCase transactionList;

  @GET
  public Response listTransactions(
      @QueryParam("accountId") @NotBlank(message = "accountId is required") String accountId,
      @QueryParam("page") @Min(value = 1, message = "page must be greater than 0") Integer page,
      @QueryParam("pageSize") @Min(value = 1, message = "pageSize must be greater than 0") @Max(value = 100, message = "pageSize must be less than 100") Integer pageSize) {
    try {
      return Response.ok(transactionList.listTransactions(accountId, page, pageSize)).build();
    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(new ErrorResponse("Error listing transactions: " + e.getMessage()))
          .build();
    }
  }

  @POST
  @Path("/sync")
  public Response synchronizeTransactions(@Valid SyncRequest request) {
    try {
      transactionSynchronizer.synchronizeTransactions(request.accountId);
      return Response.ok(new SyncResponse("Transactions synchronized successfully")).build();
    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(new ErrorResponse("Error synchronizing transactions: " + e.getMessage()))
          .build();
    }
  }

  public static class SyncRequest {
    @NotBlank(message = "accountId is required")
    public String accountId;
  }

  public static class SyncResponse {
    public String message;

    public SyncResponse(String message) {
      this.message = message;
    }
  }

  public static class ErrorResponse {
    public String error;

    public ErrorResponse(String error) {
      this.error = error;
    }
  }
}
