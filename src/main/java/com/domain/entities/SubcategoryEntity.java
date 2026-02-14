package com.domain.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "subcategories")
public class SubcategoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "pluggy_id")
  private String pluggyId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  @JsonIgnore
  private CategoryEntity category;

  @Column(name = "client_concepts_cash_flow_id", nullable = false)
  private Long clientConceptsCashFlowId;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public SubcategoryEntity() {
  }

  public SubcategoryEntity(String name, String pluggyId, CategoryEntity category, Long clientConceptsCashFlowId) {
    this.name = name;
    this.pluggyId = pluggyId;
    this.category = category;
    this.clientConceptsCashFlowId = clientConceptsCashFlowId;
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPluggyId() {
    return pluggyId;
  }

  public CategoryEntity getCategory() {
    return category;
  }

  public Long getClientConceptsCashFlowId() {
    return clientConceptsCashFlowId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPluggyId(String pluggyId) {
    this.pluggyId = pluggyId;
  }

  public void setCategory(CategoryEntity category) {
    this.category = category;
  }

  public void setClientConceptsCashFlowId(Long clientConceptsCashFlowId) {
    this.clientConceptsCashFlowId = clientConceptsCashFlowId;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
