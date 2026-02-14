package com.domain.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;

@Entity
@Table(name = "account_items")
public class AccountItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "amount_currency", nullable = false)
  private String amountCurrency = "br";

  @Column(name = "amount_subcents", nullable = false)
  private int amountSubcents = 0;

  @Column(name = "item_type")
  private String itemType;

  private String name;

  @Column(name = "relatable_type")
  private String relatableType;

  @Column(nullable = false)
  private boolean synchronized_ = true;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_concepts_pluggy_account_connection_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))
  @JsonIgnore
  private AccountEntity account;

  @Column(name = "item_id")
  private String itemId;

  @Column(name = "relatable_id")
  private Long relatableId;

  public AccountItemEntity() {
  }

  public AccountItemEntity(AccountEntity account, String itemId, String name) {
    this.account = account;
    this.itemId = itemId;
    this.name = name;
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  public Long getId() {
    return id;
  }

  public String getAmountCurrency() {
    return amountCurrency;
  }

  public int getAmountSubcents() {
    return amountSubcents;
  }

  public String getItemType() {
    return itemType;
  }

  public String getName() {
    return name;
  }

  public String getRelatableType() {
    return relatableType;
  }

  public boolean isSynchronized() {
    return synchronized_;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public AccountEntity getAccount() {
    return account;
  }

  public String getItemId() {
    return itemId;
  }

  public Long getRelatableId() {
    return relatableId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setAmountCurrency(String amountCurrency) {
    this.amountCurrency = amountCurrency;
  }

  public void setAmountSubcents(int amountSubcents) {
    this.amountSubcents = amountSubcents;
  }

  public void setItemType(String itemType) {
    this.itemType = itemType;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRelatableType(String relatableType) {
    this.relatableType = relatableType;
  }

  public void setSynchronized(boolean synchronized_) {
    this.synchronized_ = synchronized_;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setAccount(AccountEntity account) {
    this.account = account;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public void setRelatableId(Long relatableId) {
    this.relatableId = relatableId;
  }
}
