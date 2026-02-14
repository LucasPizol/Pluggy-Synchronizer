package com.domain.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(nullable = false)
  private String name;

  private String status;

  @Column(name = "synchronized_at")
  private LocalDateTime synchronizedAt;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "client_concept_id", nullable = false)
  private Long clientConceptId;

  @Column(name = "connection_id")
  private String connectionId;

  @OneToMany(mappedBy = "account")
  @JsonIgnore
  private List<AccountItemEntity> accountItems;

  public AccountEntity() {
  }

  public AccountEntity(Long clientConceptId, String name, String connectionId) {
    this.clientConceptId = clientConceptId;
    this.name = name;
    this.connectionId = connectionId;
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  public Long getId() {
    return id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getName() {
    return name;
  }

  public String getStatus() {
    return status;
  }

  public LocalDateTime getSynchronizedAt() {
    return synchronizedAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public Long getClientConceptId() {
    return clientConceptId;
  }

  public String getConnectionId() {
    return connectionId;
  }

  public List<AccountItemEntity> getAccountItems() {
    return accountItems;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setSynchronizedAt(LocalDateTime synchronizedAt) {
    this.synchronizedAt = synchronizedAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setClientConceptId(Long clientConceptId) {
    this.clientConceptId = clientConceptId;
  }

  public void setConnectionId(String connectionId) {
    this.connectionId = connectionId;
  }

  public void setAccountItems(List<AccountItemEntity> accountItems) {
    this.accountItems = accountItems;
  }
}
