package com.domain.gateway.open_finance.models;

public class TransactionPageResponse {
  private int total;
  private int totalPages;
  private int page;
  private Transaction[] transactions;

  public TransactionPageResponse() {
  }

  public TransactionPageResponse(int total, int totalPages, int page, Transaction[] transactions) {
    this.total = total;
    this.totalPages = totalPages;
    this.page = page;
    this.transactions = transactions;
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

  public Transaction[] getTransactions() {
    return transactions;
  }
}
