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
@Table(name = "categories")
public class CategoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "category_type")
  private String categoryType;

  private String color;

  @Column(name = "is_default", nullable = false)
  private boolean default_ = false;

  private String icon;

  private String name;

  @Column(nullable = false)
  private String status = "visible";

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "client_concepts_cash_flow_id", nullable = false)
  private Long clientConceptsCashFlowId;

  @Column(name = "pluggy_id")
  private String pluggyId;

  @OneToMany(mappedBy = "category")
  @JsonIgnore
  private List<SubcategoryEntity> subcategories;

  public CategoryEntity() {
  }

  public CategoryEntity(String name, Long clientConceptsCashFlowId, String pluggyId) {
    this.name = name;
    this.clientConceptsCashFlowId = clientConceptsCashFlowId;
    this.pluggyId = pluggyId;
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  public Long getId() {
    return id;
  }

  public String getCategoryType() {
    return categoryType;
  }

  public String getColor() {
    return color;
  }

  public boolean isDefault() {
    return default_;
  }

  public String getIcon() {
    return icon;
  }

  public String getName() {
    return name;
  }

  public String getStatus() {
    return status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public Long getClientConceptsCashFlowId() {
    return clientConceptsCashFlowId;
  }

  public String getPluggyId() {
    return pluggyId;
  }

  public List<SubcategoryEntity> getSubcategories() {
    return subcategories;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCategoryType(String categoryType) {
    this.categoryType = categoryType;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public void setDefault(boolean default_) {
    this.default_ = default_;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setClientConceptsCashFlowId(Long clientConceptsCashFlowId) {
    this.clientConceptsCashFlowId = clientConceptsCashFlowId;
  }

  public void setPluggyId(String pluggyId) {
    this.pluggyId = pluggyId;
  }

  public void setSubcategories(List<SubcategoryEntity> subcategories) {
    this.subcategories = subcategories;
  }
}
