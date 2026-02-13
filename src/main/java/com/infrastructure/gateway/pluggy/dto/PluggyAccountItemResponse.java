package com.infrastructure.gateway.pluggy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PluggyAccountItemResponse {
  private int total;
  private int totalPages;
  private int page;
  private PluggyAccountItem[] results;

  public PluggyAccountItemResponse() {
  }

  public PluggyAccountItemResponse(int total, int totalPages, int page, PluggyAccountItem[] results) {
    this.total = total;
    this.totalPages = totalPages;
    this.page = page;
    this.results = results;
  }

  public int getTotal() {
    return total;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public int getPage() {
    return page;
  }

  public PluggyAccountItem[] getResults() {
    return results;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public void setResults(PluggyAccountItem[] results) {
    this.results = results;
  }
}
