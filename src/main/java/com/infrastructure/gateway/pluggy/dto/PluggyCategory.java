package com.infrastructure.gateway.pluggy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PluggyCategory {
  private String id;
  private String description;

  @JsonProperty("descriptionTranslated")
  private String descriptionTranslated;

  @JsonProperty("parentId")
  private String parentId;

  @JsonProperty("parentDescription")
  private String parentDescription;

  public PluggyCategory() {
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
