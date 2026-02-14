package com.resources;

import com.domain.usecase.openfinance.IAccountSynchronizerUseCase;
import com.application.dto.SynchronizeCashFlowDTO;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;

@Path("/sync-account")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SynchronizeCashFlow {
  @Inject
  private IAccountSynchronizerUseCase accountSynchronizerUseCase;

  @POST
  @Transactional
  public Response synchronizeCashFlow(@Valid SynchronizeCashFlowDTO dto) {
    try {

      accountSynchronizerUseCase.synchronizeAccount(dto.getConceptId(), dto.getCashFlowId(), dto.getAccountId());

      return Response.ok().build();
    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(new ErrorResponse(e.getMessage()))
          .type(MediaType.APPLICATION_JSON)
          .build();
    }
  }
}

class ErrorResponse {
  public String message;

  public ErrorResponse(String message) {
    this.message = message;
  }
}
