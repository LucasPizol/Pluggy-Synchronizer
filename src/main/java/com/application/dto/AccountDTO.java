package com.application.dto;

import com.infrastructure.persistence.entities.AccountEntity;

public class AccountDTO {
  private String id;
  private String name;
  private String primaryColor;
  private String institutionLogo;
  private Integer institutionId;

  public AccountDTO() {
  }

  public static AccountDTO fromEntity(AccountEntity entity) {
    if (entity == null) return null;
    AccountDTO dto = new AccountDTO();
    dto.id = entity.getId();
    dto.name = entity.getName();
    dto.primaryColor = entity.getPrimaryColor();
    dto.institutionLogo = entity.getInstitutionLogo();
    dto.institutionId = entity.getInstitutionId();
    return dto;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPrimaryColor() {
    return primaryColor;
  }

  public String getInstitutionLogo() {
    return institutionLogo;
  }

  public Integer getInstitutionId() {
    return institutionId;
  }
}
