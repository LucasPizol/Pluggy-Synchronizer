package com.infrastructure.gateway.pluggy.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PluggyAccountResponse {
  private String id;
  private LocalDateTime nextAutoSyncAt;
  private String status;
  private String executionStatus;
  private String clientUserId;
  private PluggyAccount connector;

  public PluggyAccountResponse() {
  }

  public PluggyAccountResponse(String id, LocalDateTime nextAutoSyncAt, String status, String executionStatus,
      String clientUserId,
      PluggyAccount account) {
    this.id = id;
    this.nextAutoSyncAt = nextAutoSyncAt;
    this.status = status;
    this.executionStatus = executionStatus;
    this.clientUserId = clientUserId;
    this.connector = account;
  }

  public String getId() {
    return id;
  }

  public LocalDateTime getNextAutoSyncAt() {
    return nextAutoSyncAt;
  }

  public String getStatus() {
    return status;
  }

  public String getExecutionStatus() {
    return executionStatus;
  }

  public String getClientUserId() {
    return clientUserId;
  }

  public PluggyAccount getConnector() {
    return connector;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setNextAutoSyncAt(LocalDateTime nextAutoSyncAt) {
    this.nextAutoSyncAt = nextAutoSyncAt;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setExecutionStatus(String executionStatus) {
    this.executionStatus = executionStatus;
  }

  public void setClientUserId(String clientUserId) {
    this.clientUserId = clientUserId;
  }

  public void setConnector(PluggyAccount connector) {
    this.connector = connector;
  }
}
