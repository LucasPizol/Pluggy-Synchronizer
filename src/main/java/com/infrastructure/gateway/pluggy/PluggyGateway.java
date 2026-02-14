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
import com.domain.gateway.openfinance.models.OpenFinanceAccountItem;
import com.domain.gateway.openfinance.models.OpenFinanceCategory;
import com.domain.gateway.openfinance.models.OpenFinanceTransaction;
import com.domain.shared.PaginatedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.infrastructure.gateway.pluggy.auth.PluggyAuthContext;
import com.infrastructure.gateway.pluggy.auth.RequiresPluggyAuth;
import com.infrastructure.gateway.pluggy.dto.PluggyAccount;
import com.infrastructure.gateway.pluggy.dto.PluggyAccountItem;
import com.infrastructure.gateway.pluggy.dto.PluggyAccountItemResponse;
import com.infrastructure.gateway.pluggy.dto.PluggyAccountResponse;
import com.infrastructure.gateway.pluggy.dto.PluggyCategoriesResponse;
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
  @RequiresPluggyAuth
  public OpenFinanceAccount getAccount(String accountId) {
    try {
      HttpRequest request = buildRequest("/items/" + accountId).GET().build();
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      PluggyAccountResponse pluggyAccountResponse = objectMapper.readValue(response.body(),
          PluggyAccountResponse.class);

      LOG.infof("Account response: %s", response.body());

      PluggyAccount pluggyAccount = pluggyAccountResponse.getConnector();

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

  @Override
  @RequiresPluggyAuth
  public OpenFinanceAccountItem[] listAccountItems(String accountId) {
    try {
      HttpRequest request = buildRequest("/accounts?itemId=" + accountId).GET().build();
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      LOG.infof("Item Response response: %s", response.body());

      PluggyAccountItemResponse pluggyAccountItemResponse = objectMapper.readValue(response.body(),
          PluggyAccountItemResponse.class);

      PluggyAccountItem[] pluggyAccountItems = pluggyAccountItemResponse.getResults();

      return Arrays.stream(pluggyAccountItems)
          .map(pluggyAccountItem -> new OpenFinanceAccountItem(
              pluggyAccountItem.getId(),
              pluggyAccountItem.getName(),
              pluggyAccountItem.getBalance()))
          .toArray(OpenFinanceAccountItem[]::new);
    } catch (Exception e) {
      LOG.error("Falha ao listar contas", e);
      throw new RuntimeException("Failed to list account items", e);
    }
  }

  @Override
  @RequiresPluggyAuth
  public PaginatedResponse<OpenFinanceCategory> listCategories(int page) {
    try {
      String path = "/categories?page=" + page;
      HttpRequest request = buildRequest(path).GET().build();
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        PluggyCategoriesResponse pluggyResponse = objectMapper.readValue(response.body(),
            PluggyCategoriesResponse.class);

        OpenFinanceCategory[] categories = pluggyResponse.getResults() == null
            ? new OpenFinanceCategory[0]
            : Arrays.stream(pluggyResponse.getResults())
                .map(p -> new OpenFinanceCategory(
                    p.getId(),
                    p.getDescription(),
                    p.getDescriptionTranslated(),
                    p.getParentId(),
                    p.getParentDescription()))
                .toArray(OpenFinanceCategory[]::new);

        return new PaginatedResponse<>(
            pluggyResponse.getPage(),
            500,
            pluggyResponse.getTotal(),
            pluggyResponse.getTotalPages(),
            categories);
      } else if (response.statusCode() == 401) {
        throw new RuntimeException("Unauthorized - invalid token");
      } else {
        LOG.errorf("Failed to list categories: %d - %s", response.statusCode(), response.body());
        throw new RuntimeException("Failed to list categories: " + response.statusCode());
      }
    } catch (Exception e) {
      LOG.error("Falha ao listar categorias", e);
      throw new RuntimeException("Failed to list categories", e);
    }
  }
}
