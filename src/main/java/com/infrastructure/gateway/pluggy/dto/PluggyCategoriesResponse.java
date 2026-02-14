package com.infrastructure.gateway.pluggy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PluggyCategoriesResponse {
  private int page;
  private int total;
  private int totalPages;
  private PluggyCategory[] results;

  public PluggyCategoriesResponse() {
  }

  public int getPage() {
    return page;
  }

  public int getTotal() {
    return total;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public PluggyCategory[] getResults() {
    return results;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public void setResults(PluggyCategory[] results) {
    this.results = results;
  }
}
