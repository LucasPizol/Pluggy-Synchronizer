package com.resources;

import com.domain.usecase.accountconnection.IGetAccountUseCase;
import com.domain.entities.AccountEntity;
import com.resources.TransactionsSynchronizer.ErrorResponse;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/account-connection")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountConnection {
  @Inject
  private IGetAccountUseCase getAccountUseCase;

  @GET
  public Response getAccount(
      @QueryParam("accountId") @NotBlank(message = "accountId is required") String accountId) {
    try {
      AccountEntity account = getAccountUseCase.getAccount(accountId);
      if (account == null) {
        return Response.status(Response.Status.NOT_FOUND)
            .entity(new ErrorResponse("Account not found"))
            .build();
      }

      return Response.ok(account).build();
    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(new ErrorResponse("Error getting account: " + e.getMessage()))
          .build();
    }
  }
}
