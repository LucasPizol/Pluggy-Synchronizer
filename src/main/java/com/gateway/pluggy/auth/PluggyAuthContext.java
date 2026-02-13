package com.gateway.pluggy.auth;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class PluggyAuthContext {
  private String token;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getBearerToken() {
    return token != null ? "Bearer " + token : null;
  }

  public void clear() {
    this.token = null;
  }
}
