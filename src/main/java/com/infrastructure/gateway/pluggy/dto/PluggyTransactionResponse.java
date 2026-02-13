package com.infrastructure.gateway.pluggy.dto;

public class PluggyTransactionResponse {
  private int total;
  private int totalPages;
  private int page;
  private PluggyTransaction[] results;

  public PluggyTransactionResponse() {
  }

  public PluggyTransactionResponse(int total, int totalPages, int page, PluggyTransaction[] results) {
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

  public PluggyTransaction[] getResults() {
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

  public void setResults(PluggyTransaction[] results) {
    this.results = results;
  }
}
