package com.domain.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class AccountEntity {

  @PrePersist
  public void prePersist() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }

  @Id
  @Column(length = 36)
  private String id;

  @Column(nullable = false)
  private String name;

  @Column(name = "primary_color", length = 7)
  private String primaryColor;

  @Column(name = "institution_logo")
  private String institutionLogo;

  @Column(name = "institution_id")
  private Integer institutionId;

  @Column(name = "account_id", nullable = false, length = 36)
  private String accountId;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "account")
  @JsonIgnore
  private List<AccountItemEntity> accountItems;

  public AccountEntity() {
  }

  public AccountEntity(String name, String primaryColor, String institutionLogo,
      Integer institutionId, String accountId) {
    this.name = name;
    this.primaryColor = primaryColor;
    this.institutionLogo = institutionLogo;
    this.institutionId = institutionId;
    this.accountId = accountId;
    this.createdAt = LocalDateTime.now();
  }

  public AccountEntity(String id, String name, String primaryColor, String institutionLogo,
      Integer institutionId, String accountId) {
    this.id = id;
    this.name = name;
    this.primaryColor = primaryColor;
    this.institutionLogo = institutionLogo;
    this.institutionId = institutionId;
    this.accountId = accountId;
    this.createdAt = LocalDateTime.now();
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public List<AccountItemEntity> getAccountItems() {
    return accountItems;
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

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
