package com.infrastructure.gateway.pluggy.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PluggyTransaction {
  private String id;
  private String accountId;
  private String description;
  private String descriptionRaw;
  private String currencyCode;
  private double amount;
  private Double amountInAccountCurrency;
  private OffsetDateTime date;
  private Double balance;
  private String category;
  private String providerCode;
  private String status;
  private String type;
  private String providerId;
  private String operationType;

  public PluggyTransaction() {
  }

  // Getters
  public String getId() {
    return id;
  }

  public String getAccountId() {
    return accountId;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionRaw() {
    return descriptionRaw;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public double getAmount() {
    return amount;
  }

  public Double getAmountInAccountCurrency() {
    return amountInAccountCurrency;
  }

  public OffsetDateTime getDate() {
    return date;
  }

  public Double getBalance() {
    return balance;
  }

  public String getCategory() {
    return category;
  }

  public String getProviderCode() {
    return providerCode;
  }

  public String getStatus() {
    return status;
  }

  public String getType() {
    return type;
  }

  public String getProviderId() {
    return providerId;
  }

  public String getOperationType() {
    return operationType;
  }

  // Setters
  public void setId(String id) {
    this.id = id;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDescriptionRaw(String descriptionRaw) {
    this.descriptionRaw = descriptionRaw;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setAmountInAccountCurrency(Double amountInAccountCurrency) {
    this.amountInAccountCurrency = amountInAccountCurrency;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setProviderCode(String providerCode) {
    this.providerCode = providerCode;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }
}
