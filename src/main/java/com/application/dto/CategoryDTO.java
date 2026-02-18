package com.application.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.domain.entities.CategoryEntity;

public class CategoryDTO {
  private Long id;
  private String name;
  private String originalName;
  private String pluggyId;
  private List<SubcategoryDTO> subcategories = new ArrayList<>();

  public CategoryDTO() {
  }

  public static CategoryDTO fromEntity(CategoryEntity entity) {
    if (entity == null)
      return null;
    CategoryDTO dto = new CategoryDTO();
    dto.id = entity.getId();
    dto.name = entity.getName();
    dto.originalName = entity.getOriginalName();
    dto.pluggyId = entity.getPluggyId();
    if (entity.getSubcategories() != null) {
      dto.subcategories = entity.getSubcategories().stream()
          .map(SubcategoryDTO::fromEntity)
          .collect(Collectors.toList());
    }
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

  public String getOriginalName() {
    return originalName;
  }

  public List<SubcategoryDTO> getSubcategories() {
    return subcategories;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOriginalName(String originalName) {
    this.originalName = originalName;
  }

  public void setPluggyId(String pluggyId) {
    this.pluggyId = pluggyId;
  }

  public void setSubcategories(List<SubcategoryDTO> subcategories) {
    this.subcategories = subcategories;
  }
}
