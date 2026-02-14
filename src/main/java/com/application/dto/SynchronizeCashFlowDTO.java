package com.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SynchronizeCashFlowDTO {
  @NotNull(message = "conceptId is required")
  private Integer conceptId;

  @NotNull(message = "cashFlowId is required")
  private Long cashFlowId;

  @NotBlank(message = "accountId is required")
  private String accountId;

  public SynchronizeCashFlowDTO() {
  }

  public Integer getConceptId() {
    return conceptId;
  }

  public Long getCashFlowId() {
    return cashFlowId;
  }

  public String getAccountId() {
    return accountId;
  }
}
