package com.application.dto;

import java.time.LocalDateTime;

import com.domain.entities.TransactionEntity;

public class TransactionDTO {
  private String id;
  private String description;
  private double amount;
  private LocalDateTime date;
  private String status;
  private String type;
  private Integer categoryId;
  private String providerId;
  private String integrationId;
  private LocalDateTime createdAt;
  private AccountItemDTO accountItem;

  public TransactionDTO() {
  }

  public static TransactionDTO fromEntity(TransactionEntity entity) {
    if (entity == null) return null;
    TransactionDTO dto = new TransactionDTO();
    dto.id = entity.getId();
    dto.description = entity.getDescription();
    dto.amount = entity.getAmount();
    dto.date = entity.getDate();
    dto.status = entity.getStatus();
    dto.type = entity.getType();
    dto.categoryId = entity.getCategoryId();
    dto.providerId = entity.getProviderId();
    dto.integrationId = entity.getIntegrationId();
    dto.createdAt = entity.getCreatedAt();
    dto.accountItem = AccountItemDTO.fromEntity(entity.getAccountItem());
    return dto;
  }

  public String getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public double getAmount() {
    return amount;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public String getStatus() {
    return status;
  }

  public String getType() {
    return type;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public String getProviderId() {
    return providerId;
  }

  public String getIntegrationId() {
    return integrationId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public AccountItemDTO getAccountItem() {
    return accountItem;
  }
}
