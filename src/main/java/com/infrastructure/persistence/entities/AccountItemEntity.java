package com.infrastructure.persistence.entities;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;

@Entity
@Table(name = "account_items")
public class AccountItemEntity {
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
  private String integrationId;

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_item_account", value = ConstraintMode.CONSTRAINT))
  @JsonIgnore
  private AccountEntity account;

  @OneToMany(mappedBy = "accountItem")
  @JsonIgnore
  private List<TransactionEntity> transactions;

  public AccountItemEntity() {
  }

  public AccountItemEntity(AccountEntity account, String integrationId, String name) {
    this.account = account;
    this.integrationId = integrationId;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public AccountEntity getAccount() {
    return account;
  }

  public String getIntegrationId() {
    return integrationId;
  }

  public String getName() {
    return name;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setAccount(AccountEntity account) {
    this.account = account;
  }

  public void setIntegrationId(String integrationId) {
    this.integrationId = integrationId;
  }

  public void setName(String name) {
    this.name = name;
  }
}
