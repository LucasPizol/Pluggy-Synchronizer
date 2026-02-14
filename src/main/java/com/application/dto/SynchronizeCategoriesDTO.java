package com.application.dto;

import jakarta.validation.constraints.NotNull;

public class SynchronizeCategoriesDTO {

  @NotNull(message = "conceptId is required")
  private Integer conceptId;

  public SynchronizeCategoriesDTO() {
  }

  public Integer getConceptId() {
    return conceptId;
  }

  public void setConceptId(Integer conceptId) {
    this.conceptId = conceptId;
  }
}
