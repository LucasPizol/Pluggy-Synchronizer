package com.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.domain.entities.TransactionEntity;

public class TransactionDTO {
  private Long id;
  private boolean blocked;
  private String details;
  private String entryMode;
  private Integer installmentNumber;
  private String name;
  private String originalValue;
  private String tempValue;
  private LocalDate transactionDate;
  private String transactionType;
  private String value;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isSynchronized;
  private TransactionCategoryDTO category;
  private TransactionCategoryDTO subcategory;

  public TransactionDTO() {
  }

  public static TransactionDTO fromEntity(TransactionEntity entity) {
    if (entity == null)
      return null;
    TransactionDTO dto = new TransactionDTO();
    dto.id = entity.getId();
    dto.blocked = entity.isBlocked();
    dto.details = entity.getDetails();
    dto.entryMode = entity.getEntryMode();
    dto.installmentNumber = entity.getInstallmentNumber();
    dto.name = entity.getName();
    dto.originalValue = entity.getOriginalValue().toString();
    dto.tempValue = entity.getTempValue().toString();
    dto.transactionDate = entity.getTransactionDate();
    dto.transactionType = entity.getTransactionType();
    dto.value = entity.getValue().toString();
    dto.createdAt = entity.getCreatedAt();
    dto.updatedAt = entity.getUpdatedAt();
    dto.isSynchronized = entity.getIntegrationId() != null && !entity.getIntegrationId().isBlank();
    return dto;
  }

  public Long getId() {
    return id;
  }

  public boolean isBlocked() {
    return blocked;
  }

  public String getDetails() {
    return details;
  }

  public String getEntryMode() {
    return entryMode;
  }

  public Integer getInstallmentNumber() {
    return installmentNumber;
  }

  public String getName() {
    return name;
  }

  public String getOriginalValue() {
    return originalValue;
  }

  public String getTempValue() {
    return tempValue;
  }

  public LocalDate getTransactionDate() {
    return transactionDate;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public String getValue() {
    return value;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public Boolean isSynchronized() {
    return isSynchronized;
  }

  public TransactionCategoryDTO getCategory() {
    return category;
  }

  public TransactionCategoryDTO getSubcategory() {
    return subcategory;
  }

  public void setCategory(TransactionCategoryDTO category) {
    this.category = category;
  }

  public void setSubcategory(TransactionCategoryDTO subcategory) {
    this.subcategory = subcategory;
  }
}
