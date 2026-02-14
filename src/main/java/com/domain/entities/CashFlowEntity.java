package com.domain.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cash_flows")
public class CashFlowEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private boolean blocked = false;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "client_concept_id", nullable = false)
  private Long clientConceptId;

  public CashFlowEntity() {
  }

  public CashFlowEntity(Long clientConceptId) {
    this.clientConceptId = clientConceptId;
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  public Long getId() {
    return id;
  }

  public boolean isBlocked() {
    return blocked;
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

  public void setId(Long id) {
    this.id = id;
  }

  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
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
}
