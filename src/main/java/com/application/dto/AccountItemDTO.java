package com.application.dto;

import java.time.LocalDateTime;

import com.domain.entities.AccountItemEntity;

public class AccountItemDTO {
  private Long id;
  private String amountCurrency;
  private int amountSubcents;
  private String itemType;
  private String name;
  private String relatableType;
  private boolean synchronized_;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private AccountDTO account;
  private String itemId;
  private Long relatableId;

  public AccountItemDTO() {
  }

  public static AccountItemDTO fromEntity(AccountItemEntity entity) {
    if (entity == null)
      return null;
    AccountItemDTO dto = new AccountItemDTO();
    dto.id = entity.getId();
    dto.amountCurrency = entity.getAmountCurrency();
    dto.amountSubcents = entity.getAmountSubcents();
    dto.itemType = entity.getItemType();
    dto.name = entity.getName();
    dto.relatableType = entity.getRelatableType();
    dto.synchronized_ = entity.isSynchronized();
    dto.createdAt = entity.getCreatedAt();
    dto.updatedAt = entity.getUpdatedAt();
    dto.account = AccountDTO.fromEntity(entity.getAccount());
    dto.itemId = entity.getItemId();
    dto.relatableId = entity.getRelatableId();
    return dto;
  }

  public Long getId() {
    return id;
  }

  public String getAmountCurrency() {
    return amountCurrency;
  }

  public int getAmountSubcents() {
    return amountSubcents;
  }

  public String getItemType() {
    return itemType;
  }

  public String getName() {
    return name;
  }

  public String getRelatableType() {
    return relatableType;
  }

  public boolean isSynchronized() {
    return synchronized_;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public AccountDTO getAccount() {
    return account;
  }

  public String getItemId() {
    return itemId;
  }

  public Long getRelatableId() {
    return relatableId;
  }
}
