package com.application.dto;

import com.domain.entities.CategoryEntity;
import com.domain.entities.SubcategoryEntity;

public class TransactionCategoryDTO {
  private Long id;
  private String name;

  public TransactionCategoryDTO() {
  }

  public TransactionCategoryDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public static TransactionCategoryDTO fromCategory(CategoryEntity entity) {
    if (entity == null) return null;
    return new TransactionCategoryDTO(entity.getId(), entity.getName());
  }

  public static TransactionCategoryDTO fromSubcategory(SubcategoryEntity entity) {
    if (entity == null) return null;
    return new TransactionCategoryDTO(entity.getId(), entity.getName());
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }
}
