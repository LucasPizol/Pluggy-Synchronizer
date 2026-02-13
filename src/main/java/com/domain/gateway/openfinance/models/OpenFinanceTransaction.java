package com.domain.gateway.openfinance.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFinanceTransaction {
  private String id;
  private String accountId;
  private String description;
  private double amount;
  private LocalDateTime date;
  private String status;
  private String type;
  private String category;
  private String providerId;

  public OpenFinanceTransaction() {
  }

  public OpenFinanceTransaction(String id, String accountId, String description, double amount, LocalDateTime date,
      String status,
      String type, String category, String providerId) {
    this.id = id;
    this.accountId = accountId;
    this.description = description;
    this.amount = amount;
    this.date = date;
    this.status = status;
    this.type = type;
    this.category = category;
    this.providerId = providerId;
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

  public String getCategory() {
    return category;
  }

  public String getProviderId() {
    return providerId;
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

  public void setCategory(String category) {
    this.category = category;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

}
