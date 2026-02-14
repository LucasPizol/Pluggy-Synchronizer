package com.application.dto;

import com.domain.entities.SubcategoryEntity;

public class SubcategoryDTO {
  private Long id;
  private String name;
  private String pluggyId;
  private Long categoryId;

  public SubcategoryDTO() {
  }

  public static SubcategoryDTO fromEntity(SubcategoryEntity entity) {
    if (entity == null)
      return null;
    SubcategoryDTO dto = new SubcategoryDTO();
    dto.id = entity.getId();
    dto.name = entity.getName();
    dto.pluggyId = entity.getPluggyId();
    dto.categoryId = entity.getCategory() != null ? entity.getCategory().getId() : null;
    return dto;
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

  public Long getCategoryId() {
    return categoryId;
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

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }
}
