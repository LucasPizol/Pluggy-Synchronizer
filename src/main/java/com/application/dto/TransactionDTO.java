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
  private String originalValueCurrency;
  private long originalValueSubcents;
  private String tempValueCurrency;
  private long tempValueSubcents;
  private LocalDate transactionDate;
  private String transactionType;
  private String valueCurrency;
  private long valueSubcents;
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
    dto.originalValueCurrency = entity.getOriginalValueCurrency();
    dto.originalValueSubcents = entity.getOriginalValueSubcents();
    dto.tempValueCurrency = entity.getTempValueCurrency();
    dto.tempValueSubcents = entity.getTempValueSubcents();
    dto.transactionDate = entity.getTransactionDate();
    dto.transactionType = entity.getTransactionType();
    dto.valueCurrency = entity.getValueCurrency();
    dto.valueSubcents = entity.getValueSubcents();
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

  public String getOriginalValueCurrency() {
    return originalValueCurrency;
  }

  public long getOriginalValueSubcents() {
    return originalValueSubcents;
  }

  public String getTempValueCurrency() {
    return tempValueCurrency;
  }

  public long getTempValueSubcents() {
    return tempValueSubcents;
  }

  public LocalDate getTransactionDate() {
    return transactionDate;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public String getValueCurrency() {
    return valueCurrency;
  }

  public long getValueSubcents() {
    return valueSubcents;
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
