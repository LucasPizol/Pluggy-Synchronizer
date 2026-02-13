package com.domain.shared;

public class PaginatedResponse<T> {
  private int page;
  private int pageSize;
  private long total;
  private int totalPages;
  private T[] items;

  public PaginatedResponse(int page, int pageSize, long total, int totalPages, T[] items) {
    this.page = page;
    this.pageSize = pageSize;
    this.total = total;
    this.totalPages = totalPages;
    this.items = items;
  }

  public int getPage() {
    return page;
  }

  public int getPageSize() {
    return pageSize;
  }

  public long getTotal() {
    return total;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public T[] getItems() {
    return items;
  }
}
