package com.infrastructure.gateway.pluggy;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.OpenFinanceAccount;
import com.domain.gateway.openfinance.models.OpenFinanceTransaction;
import com.domain.shared.PaginatedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.infrastructure.gateway.pluggy.auth.PluggyAuthContext;
import com.infrastructure.gateway.pluggy.auth.RequiresPluggyAuth;
import com.infrastructure.gateway.pluggy.dto.PluggyAccount;
import com.infrastructure.gateway.pluggy.dto.PluggyAccountResponse;
import com.infrastructure.gateway.pluggy.dto.PluggyTransactionResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

@ApplicationScoped
public class PluggyGateway implements IOpenFinance {
  private static final Logger LOG = Logger.getLogger(PluggyGateway.class);
  @ConfigProperty(name = "pluggy.api.url")
  String apiUrl;

  @Inject
  Provider<PluggyAuthContext> authContextProvider;

  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  public PluggyGateway() {
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
  public PaginatedResponse<OpenFinanceTransaction> listTransactions(String accountId) {
    return listTransactions(accountId, null, null);
  }

  @Override
  public PaginatedResponse<OpenFinanceTransaction> listTransactions(String accountId, LocalDate startDate) {
    return listTransactions(accountId, startDate, null);
  }

  @Override
  @RequiresPluggyAuth
  public PaginatedResponse<OpenFinanceTransaction> listTransactions(String accountId, LocalDate startDate,
      String[] transactionIds) {
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
        PluggyTransactionResponse pluggyTransactionResponse = objectMapper.readValue(response.body(),
            PluggyTransactionResponse.class);

        int total = (int) pluggyTransactionResponse.getTotal();
        int totalPages = (int) pluggyTransactionResponse.getTotalPages();
        int page = (int) pluggyTransactionResponse.getPage();

        OpenFinanceTransaction[] transactions = Arrays.stream(pluggyTransactionResponse.getResults())
            .map(pluggyTransaction -> new OpenFinanceTransaction(pluggyTransaction.getId(),
                pluggyTransaction.getAccountId(),
                pluggyTransaction.getDescription(), pluggyTransaction.getAmount(),
                pluggyTransaction.getDate().toLocalDateTime(),
                pluggyTransaction.getStatus(), pluggyTransaction.getType(), pluggyTransaction.getCategory(),
                pluggyTransaction.getProviderId()))
            .toArray(OpenFinanceTransaction[]::new);

        return new PaginatedResponse<OpenFinanceTransaction>(page, 500, total, totalPages, transactions);
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

  @Override
  public OpenFinanceAccount getAccount(String accountId) {
    try {
      HttpRequest request = buildRequest("/accounts/" + accountId).GET().build();
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      PluggyAccountResponse pluggyAccountResponse = objectMapper.readValue(response.body(),
          PluggyAccountResponse.class);

      PluggyAccount pluggyAccount = pluggyAccountResponse.getAccount();

      return new OpenFinanceAccount(
          pluggyAccount.getName(),
          pluggyAccount.getPrimaryColor(),
          pluggyAccount.getImageUrl(),
          pluggyAccount.getId(),
          pluggyAccountResponse.getId());
    } catch (Exception e) {
      LOG.error("Falha ao buscar conta", e);
      throw new RuntimeException("Failed to get account", e);
    }
  }
}
