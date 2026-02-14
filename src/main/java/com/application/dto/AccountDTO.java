package com.application.dto;

import java.time.LocalDateTime;

import com.domain.entities.AccountEntity;

public class AccountDTO {
  private Long id;
  private String imageUrl;
  private String name;
  private String status;
  private LocalDateTime synchronizedAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long clientConceptId;
  private String connectionId;

  public AccountDTO() {
  }

  public static AccountDTO fromEntity(AccountEntity entity) {
    if (entity == null)
      return null;
    AccountDTO dto = new AccountDTO();
    dto.id = entity.getId();
    dto.imageUrl = entity.getImageUrl();
    dto.name = entity.getName();
    dto.status = entity.getStatus();
    dto.synchronizedAt = entity.getSynchronizedAt();
    dto.createdAt = entity.getCreatedAt();
    dto.updatedAt = entity.getUpdatedAt();
    dto.clientConceptId = entity.getClientConceptId();
    dto.connectionId = entity.getConnectionId();
    return dto;
  }

  public Long getId() {
    return id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getName() {
    return name;
  }

  public String getStatus() {
    return status;
  }

  public LocalDateTime getSynchronizedAt() {
    return synchronizedAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public Long getClientConceptId() {
    return clientConceptId;
  }

  public String getConnectionId() {
    return connectionId;

  }
}
