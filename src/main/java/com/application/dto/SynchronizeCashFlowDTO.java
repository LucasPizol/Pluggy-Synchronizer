package com.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SynchronizeCashFlowDTO {
  @NotNull(message = "conceptId is required")
  private Long conceptId;

  @NotBlank(message = "accountId is required")
  private String accountId;

  public SynchronizeCashFlowDTO() {
  }

  public Long getConceptId() {
    return conceptId;
  }

  public String getAccountId() {
    return accountId;
  }
}
