package com.domain.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private boolean blocked = false;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "deleted_by")
  private String deletedBy;

  private String details;

  @Column(name = "entry_mode", nullable = false)
  private String entryMode;

  @Column(name = "installment_number")
  private Integer installmentNumber;

  @Column(nullable = false)
  private String name;

  @Column(name = "original_value_currency", nullable = false)
  private String originalValueCurrency = "br";

  @Column(name = "original_value_subcents", nullable = false)
  private long originalValueSubcents = 0L;

  @Column(name = "temp_value_currency", nullable = false)
  private String tempValueCurrency = "br";

  @Column(name = "temp_value_subcents", nullable = false)
  private long tempValueSubcents = 0L;

  @Column(name = "transaction_date")
  private LocalDate transactionDate;

  @Column(name = "transaction_type")
  private String transactionType;

  @Column(name = "value_currency", nullable = false)
  private String valueCurrency = "br";

  @Column(name = "value_subcents", nullable = false)
  private long valueSubcents = 0L;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "client_concepts_cash_flows_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))
  @JsonIgnore
  private CashFlowEntity cashFlow;

  @Column(name = "client_concepts_cash_flow_category_id")
  private Long clientConceptsCashFlowCategoryId;

  @Column(name = "client_concepts_cash_flow_purchase_id")
  private Long clientConceptsCashFlowPurchaseId;

  @Column(name = "client_concepts_cash_flow_subcategory_id")
  private Long clientConceptsCashFlowSubcategoryId;

  @Column(name = "integration_id")
  private String integrationId;

  public TransactionEntity() {
  }

  public Long getId() {
    return id;
  }

  public boolean isBlocked() {
    return blocked;
  }

  public LocalDateTime getDeletedAt() {
    return deletedAt;
  }

  public String getDeletedBy() {
    return deletedBy;
  }

  public String getDetails() {
    return details;
  }

  public String getEntryMode() {
    return entryMode;
  }

  public Integer getInstallmentNumber() {
    return installmentNumber;
  }

  public String getName() {
    return name;
  }

  public String getOriginalValueCurrency() {
    return originalValueCurrency;
  }

  public long getOriginalValueSubcents() {
    return originalValueSubcents;
  }

  public String getTempValueCurrency() {
    return tempValueCurrency;
  }

  public long getTempValueSubcents() {
    return tempValueSubcents;
  }

  public LocalDate getTransactionDate() {
    return transactionDate;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public String getValueCurrency() {
    return valueCurrency;
  }

  public long getValueSubcents() {
    return valueSubcents;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public CashFlowEntity getCashFlow() {
    return cashFlow;
  }

  public Long getClientConceptsCashFlowCategoryId() {
    return clientConceptsCashFlowCategoryId;
  }

  public Long getClientConceptsCashFlowPurchaseId() {
    return clientConceptsCashFlowPurchaseId;
  }

  public Long getClientConceptsCashFlowSubcategoryId() {
    return clientConceptsCashFlowSubcategoryId;
  }

  public String getIntegrationId() {
    return integrationId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public void setDeletedBy(String deletedBy) {
    this.deletedBy = deletedBy;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public void setEntryMode(String entryMode) {
    this.entryMode = entryMode;
  }

  public void setInstallmentNumber(Integer installmentNumber) {
    this.installmentNumber = installmentNumber;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOriginalValueCurrency(String originalValueCurrency) {
    this.originalValueCurrency = originalValueCurrency;
  }

  public void setOriginalValueSubcents(long originalValueSubcents) {
    this.originalValueSubcents = originalValueSubcents;
  }

  public void setTempValueCurrency(String tempValueCurrency) {
    this.tempValueCurrency = tempValueCurrency;
  }

  public void setTempValueSubcents(long tempValueSubcents) {
    this.tempValueSubcents = tempValueSubcents;
  }

  public void setTransactionDate(LocalDate transactionDate) {
    this.transactionDate = transactionDate;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public void setValueCurrency(String valueCurrency) {
    this.valueCurrency = valueCurrency;
  }

  public void setValueSubcents(long valueSubcents) {
    this.valueSubcents = valueSubcents;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setCashFlow(CashFlowEntity cashFlow) {
    this.cashFlow = cashFlow;
  }

  public void setClientConceptsCashFlowCategoryId(Long clientConceptsCashFlowCategoryId) {
    this.clientConceptsCashFlowCategoryId = clientConceptsCashFlowCategoryId;
  }

  public void setClientConceptsCashFlowPurchaseId(Long clientConceptsCashFlowPurchaseId) {
    this.clientConceptsCashFlowPurchaseId = clientConceptsCashFlowPurchaseId;
  }

  public void setClientConceptsCashFlowSubcategoryId(Long clientConceptsCashFlowSubcategoryId) {
    this.clientConceptsCashFlowSubcategoryId = clientConceptsCashFlowSubcategoryId;
  }

  public void setIntegrationId(String integrationId) {
    this.integrationId = integrationId;
  }
}
