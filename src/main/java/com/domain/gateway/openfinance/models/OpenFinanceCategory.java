package com.domain.gateway.openfinance.models;

public class OpenFinanceCategory {
  private String id;
  private String description;
  private String descriptionTranslated;
  private String parentId;
  private String parentDescription;

  public OpenFinanceCategory() {
  }

  public OpenFinanceCategory(String id, String description, String descriptionTranslated,
      String parentId, String parentDescription) {
    this.id = id;
    this.description = description;
    this.descriptionTranslated = descriptionTranslated;
    this.parentId = parentId;
    this.parentDescription = parentDescription;
  }

  public String getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionTranslated() {
    return descriptionTranslated;
  }

  public String getParentId() {
    return parentId;
  }

  public String getParentDescription() {
    return parentDescription;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDescriptionTranslated(String descriptionTranslated) {
    this.descriptionTranslated = descriptionTranslated;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public void setParentDescription(String parentDescription) {
    this.parentDescription = parentDescription;
  }
}
