package com.application.dto;

import com.domain.entities.AccountItemEntity;

public class AccountItemDTO {
  private String id;
  private String integrationId;
  private String name;
  private AccountDTO account;

  public AccountItemDTO() {
  }

  public static AccountItemDTO fromEntity(AccountItemEntity entity) {
    if (entity == null) return null;
    AccountItemDTO dto = new AccountItemDTO();
    dto.id = entity.getId();
    dto.integrationId = entity.getIntegrationId();
    dto.name = entity.getName();
    dto.account = AccountDTO.fromEntity(entity.getAccount());
    return dto;
  }

  public String getId() {
    return id;
  }

  public String getIntegrationId() {
    return integrationId;
  }

  public String getName() {
    return name;
  }

  public AccountDTO getAccount() {
    return account;
  }
}
