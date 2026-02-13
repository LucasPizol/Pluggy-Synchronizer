package com.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import com.domain.usecase.openfinance.ITransactionSynchronizerUseCase;
import com.domain.usecase.transactions.ITransactionListUseCase;
import com.domain.shared.PaginatedResponse;
import com.infrastructure.persistence.entities.TransactionEntity;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@QuarkusTest
class TransactionsSynchronizerResourceTest {

  @InjectMock
  ITransactionSynchronizerUseCase transactionSynchronizer;

  @InjectMock
  ITransactionListUseCase transactionList;

  @Test
  void shouldSyncTransactionsSuccessfully() {
    doNothing().when(transactionSynchronizer).synchronizeTransactions(anyString());

    given()
        .contentType(ContentType.JSON)
        .body("{\"accountId\": \"account-123\"}")
        .when()
        .post("/transactions/sync")
        .then()
        .statusCode(200)
        .body("message", equalTo("Transactions synchronized successfully"));

    verify(transactionSynchronizer, times(1)).synchronizeTransactions("account-123");
  }

  @Test
  void shouldReturnBadRequestWhenAccountIdMissing() {
    given()
        .contentType(ContentType.JSON)
        .body("{}")
        .when()
        .post("/transactions/sync")
        .then()
        .statusCode(400);
  }

  @Test
  void shouldReturnBadRequestWhenAccountIdBlank() {
    given()
        .contentType(ContentType.JSON)
        .body("{\"accountId\": \"\"}")
        .when()
        .post("/transactions/sync")
        .then()
        .statusCode(400);
  }

  @Test
  void shouldReturnInternalErrorWhenSyncFails() {
    doThrow(new RuntimeException("API Error"))
        .when(transactionSynchronizer).synchronizeTransactions(anyString());

    given()
        .contentType(ContentType.JSON)
        .body("{\"accountId\": \"account-123\"}")
        .when()
        .post("/transactions/sync")
        .then()
        .statusCode(500)
        .body("error", containsString("Error synchronizing transactions"));
  }

  @Test
  void shouldListTransactionsSuccessfully() {
    TransactionEntity entity = new TransactionEntity(
        "tx-1", "account-123", "Test", 100.0,
        LocalDateTime.now(), "POSTED", "CREDIT", 1, null);

    PaginatedResponse<TransactionEntity> response = new PaginatedResponse<>(
        1, 10, 1L, 1, new TransactionEntity[] { entity });

    when(transactionList.listTransactions(anyString(), anyInt(), anyInt()))
        .thenReturn(response);

    given()
        .queryParam("accountId", "account-123")
        .queryParam("page", 1)
        .queryParam("pageSize", 10)
        .when()
        .get("/transactions")
        .then()
        .statusCode(200)
        .body("total", equalTo(1))
        .body("items", hasSize(1));
  }

  @Test
  void shouldReturnBadRequestWhenAccountIdMissingOnList() {
    given()
        .queryParam("page", 1)
        .queryParam("pageSize", 10)
        .when()
        .get("/transactions")
        .then()
        .statusCode(400);
  }

  @Test
  void shouldReturnBadRequestWhenPageInvalid() {
    given()
        .queryParam("accountId", "account-123")
        .queryParam("page", 0)
        .queryParam("pageSize", 10)
        .when()
        .get("/transactions")
        .then()
        .statusCode(400);
  }

  @Test
  void shouldReturnBadRequestWhenPageSizeTooLarge() {
    given()
        .queryParam("accountId", "account-123")
        .queryParam("page", 1)
        .queryParam("pageSize", 101)
        .when()
        .get("/transactions")
        .then()
        .statusCode(400);
  }
}
