package com.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

  @Id
  @Column(length = 36)
  private String id;

  @PrePersist
  public void prePersist() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }

  @ManyToOne
  @JoinColumn(name = "account_item_id", nullable = false, foreignKey = @ForeignKey(name = "fk_transaction_account_item", value = ConstraintMode.CONSTRAINT))
  @JsonIgnore
  private AccountItemEntity accountItem;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private double amount;

  @Column(nullable = false)
  private LocalDateTime date;

  @Column(nullable = false, length = 20)
  private String status;

  @Column(nullable = false, length = 20)
  private String type;

  @Column(name = "category_id", nullable = false)
  private Integer categoryId;

  @Column(name = "provider_id", length = 50)
  private String providerId;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "integration_id", length = 50)
  private String integrationId;

  public TransactionEntity() {
  }

  public TransactionEntity(AccountItemEntity accountItem, String description, double amount,
      LocalDateTime date, String status, String type, Integer categoryId, String providerId, String integrationId) {
    this.accountItem = accountItem;
    this.description = description;
    this.amount = amount;
    this.date = date;
    this.status = status;
    this.type = type;
    this.categoryId = categoryId;
    this.providerId = providerId;
    this.integrationId = integrationId;
    this.createdAt = LocalDateTime.now();
  }

  public String getId() {
    return id;
  }

  public AccountItemEntity getAccountItem() {
    return accountItem;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public String getIntegrationId() {
    return integrationId;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setAccountItem(AccountItemEntity accountItem) {
    this.accountItem = accountItem;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setIntegrationId(String integrationId) {
    this.integrationId = integrationId;
  }
}
