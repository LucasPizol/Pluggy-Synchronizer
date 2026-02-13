package com.infrastructure.persistence.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

  @Id
  @Column(length = 36)
  private String id;

  @Column(name = "account_id", nullable = false, length = 36)
  private String accountId;

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

  public TransactionEntity() {
  }

  public TransactionEntity(String id, String accountId, String description, double amount,
      LocalDateTime date, String status, String type, Integer categoryId, String providerId) {
    this.id = id;
    this.accountId = accountId;
    this.description = description;
    this.amount = amount;
    this.date = date;
    this.status = status;
    this.type = type;
    this.categoryId = categoryId;
    this.providerId = providerId;
    this.createdAt = LocalDateTime.now();
  }

  public String getId() {
    return id;
  }

  public String getAccountId() {
    return accountId;
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

  public void setId(String id) {
    this.id = id;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
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
}
