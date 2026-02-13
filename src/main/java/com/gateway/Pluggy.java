package com.gateway;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.domain.gateway.open_finance.IOpenFinance;
import com.domain.gateway.open_finance.models.Transaction;
import com.domain.gateway.open_finance.models.TransactionPageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gateway.pluggy.auth.PluggyAuthContext;
import com.gateway.pluggy.auth.RequiresPluggyAuth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

@ApplicationScoped
public class Pluggy implements IOpenFinance {
  private static final Logger LOG = Logger.getLogger(Pluggy.class);
  @ConfigProperty(name = "pluggy.api.url")
  String apiUrl;

  @Inject
  Provider<PluggyAuthContext> authContextProvider;

  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  public Pluggy() {
    this.httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  private HttpRequest.Builder buildRequest(String path) {
    return HttpRequest.newBuilder()
        .uri(URI.create(apiUrl + path))
        .header("Content-Type", "application/json")
        .header("X-API-KEY", authContextProvider.get().getToken());
  }

  @Override
  public TransactionPageResponse listTransactions(String accountId) {
    return listTransactions(accountId, null, null);
  }

  @Override
  public TransactionPageResponse listTransactions(String accountId, LocalDate startDate) {
    return listTransactions(accountId, startDate, null);
  }

  @Override
  @RequiresPluggyAuth
  public TransactionPageResponse listTransactions(String accountId, LocalDate startDate, String[] transactionIds) {
    try {
      StringBuilder pathBuilder = new StringBuilder("/transactions")
          .append("?accountId=").append(accountId);

      if (startDate != null) {
        pathBuilder.append("&from=").append(startDate.toString());
      }

      if (transactionIds != null && transactionIds.length > 0) {
        for (String id : transactionIds) {
          pathBuilder.append("&id=").append(id);
        }
      }

      HttpRequest request = buildRequest(pathBuilder.toString()).GET().build();
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        @SuppressWarnings("unchecked")
        Map<String, Object> body = objectMapper.readValue(response.body(), Map.class);

        int total = (int) body.getOrDefault("total", 0);
        int totalPages = (int) body.getOrDefault("totalPages", 0);
        int page = (int) body.getOrDefault("page", 1);

        Transaction[] transactions = objectMapper.convertValue(
            body.get("results"),
            Transaction[].class);

        return new TransactionPageResponse(total, totalPages, page, transactions);

      } else if (response.statusCode() == 401) {
        throw new RuntimeException("Unauthorized - invalid token");
      } else {
        LOG.errorf("Failed to list transactions: %d - %s", response.statusCode(), response.body());
        throw new RuntimeException("Failed to list transactions: " + response.statusCode());
      }
    } catch (Exception e) {
      LOG.error("Falha ao listar transações", e);
      throw new RuntimeException("Failed to list transactions", e);
    }
  }
}
