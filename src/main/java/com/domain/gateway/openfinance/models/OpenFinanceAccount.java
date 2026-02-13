package com.domain.gateway.openfinance.models;

public class OpenFinanceAccount {
  private String id;
  private String name;
  private String primaryColor;
  private String institutionLogo;
  private Integer institutionId;
  private String accountId;

  public OpenFinanceAccount(String name, String primaryColor, String institutionLogo,
      Integer institutionId, String accountId) {
    this.name = name;
    this.primaryColor = primaryColor;
    this.institutionLogo = institutionLogo;
    this.institutionId = institutionId;
    this.accountId = accountId;
  }

  public OpenFinanceAccount(String id, String name, String primaryColor, String institutionLogo,
      Integer institutionId, String accountId) {
    this.id = id;
    this.name = name;
    this.primaryColor = primaryColor;
    this.institutionLogo = institutionLogo;
    this.institutionId = institutionId;
    this.accountId = accountId;
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

  public String getAccountId() {
    return accountId;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrimaryColor(String primaryColor) {
    this.primaryColor = primaryColor;
  }

  public void setInstitutionLogo(String institutionLogo) {
    this.institutionLogo = institutionLogo;
  }

  public void setInstitutionId(Integer institutionId) {
    this.institutionId = institutionId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }
}
