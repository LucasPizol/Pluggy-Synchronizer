package com.infrastructure.gateway.pluggy.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PluggyAccount {
  private Integer id;
  private String name;
  private String primaryColor;
  private String institutionUrl;
  private String country;
  private String type;
  private String imageUrl;
  private Boolean oauth;
  private List<String> products;
  private Boolean isSandbox;
  private Boolean hasMFA;
  private String clientUserId;
  private Boolean isOpenFinance;

  public PluggyAccount() {
  }

  public PluggyAccount(Integer id, String name, String primaryColor, String institutionUrl, String country, String type,
      String imageUrl, Boolean oauth, List<String> products, Boolean isSandbox, Boolean hasMFA, String clientUserId,
      Boolean isOpenFinance) {
    this.id = id;
    this.name = name;
    this.primaryColor = primaryColor;
    this.institutionUrl = institutionUrl;
    this.country = country;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPrimaryColor() {
    return primaryColor;
  }

  public String getInstitutionUrl() {
    return institutionUrl;
  }

  public String getCountry() {
    return country;
  }

  public String getType() {
    return type;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public Boolean getOauth() {
    return oauth;
  }

  public List<String> getProducts() {
    return products;
  }

  public Boolean getIsSandbox() {
    return isSandbox;
  }

  public Boolean getHasMFA() {
    return hasMFA;
  }

  public String getClientUserId() {
    return clientUserId;
  }

  public Boolean getIsOpenFinance() {
    return isOpenFinance;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrimaryColor(String primaryColor) {
    this.primaryColor = primaryColor;
  }

  public void setInstitutionUrl(String institutionUrl) {
    this.institutionUrl = institutionUrl;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setOauth(Boolean oauth) {
    this.oauth = oauth;
  }

  public void setProducts(List<String> products) {
    this.products = products;
  }

  public void setIsSandbox(Boolean isSandbox) {
    this.isSandbox = isSandbox;
  }

  public void setHasMFA(Boolean hasMFA) {
    this.hasMFA = hasMFA;
  }

  public void setClientUserId(String clientUserId) {
    this.clientUserId = clientUserId;
  }

  public void setIsOpenFinance(Boolean isOpenFinance) {
    this.isOpenFinance = isOpenFinance;
  }
}
