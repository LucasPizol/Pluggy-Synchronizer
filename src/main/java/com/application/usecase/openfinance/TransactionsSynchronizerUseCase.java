package com.application.usecase.openfinance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.domain.entities.AccountEntity;
import com.domain.entities.CashFlowEntity;
import com.domain.entities.CategoryEntity;
import com.domain.entities.SubcategoryEntity;
import com.domain.entities.AccountItemEntity;
import com.domain.entities.TransactionEntity;
import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.OpenFinanceTransaction;
import com.domain.repositories.categories.ICategoryRepository;
import com.domain.repositories.subcategories.ISubcategoryRepository;
import com.domain.repositories.transactions.ITransactionRepository;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.accountitem.IListAccountItemUseCase;
import com.domain.usecase.cashflow.IUpsertCashFlowUseCase;
import com.domain.usecase.openfinance.ITransactionSynchronizerUseCase;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TransactionsSynchronizerUseCase implements ITransactionSynchronizerUseCase {
  private static final Logger LOG = Logger.getLogger(TransactionsSynchronizerUseCase.class);
  @Inject
  private IOpenFinance openFinance;

  @Inject
  private ITransactionRepository transactionRepository;

  @Inject
  private IListAccountItemUseCase listAccountItemUseCase;

  @Inject
  private IUpsertCashFlowUseCase upsertCashFlowUseCase;

  @Inject
  private ICategoryRepository categoryRepository;

  @Inject
  private ISubcategoryRepository subcategoryRepository;

  @Override
  public void synchronizeTransactions(AccountEntity account) {
    synchronizeTransactions(account, null, null);
  }

  @Override
  public void synchronizeTransactions(AccountEntity account, LocalDate startDate) {
    synchronizeTransactions(account, startDate, null);
  }

  @Override
  @Transactional
  public void synchronizeTransactions(AccountEntity account, LocalDate startDate, String[] transactionIds) {
    LOG.infof("Account: %s", account.getId());
    CashFlowEntity cashFlow = upsertCashFlowUseCase.upsertCashFlow(account.getClientConceptId());
    Long cashFlowId = cashFlow.getId();
    List<AccountItemEntity> accountItems = listAccountItemUseCase.listAccountItems(account.getId());
    LOG.infof("Account Items count: %d", accountItems.size());

    for (AccountItemEntity accountItem : accountItems) {
      PaginatedResponse<OpenFinanceTransaction> response = openFinance.listTransactions(accountItem.getItemId(),
          startDate,
          transactionIds);

      OpenFinanceTransaction[] transactions = response.getItems();
      Set<String> existingIds = getExistingTransactionIds(transactions);

      // Coletar todos os nomes de categorias das transações
      Set<String> categoryNames = Arrays.stream(transactions)
          .map(OpenFinanceTransaction::getCategory)
          .filter(c -> c != null && !c.isBlank())
          .collect(Collectors.toSet());

      // Buscar categorias e subcategorias em batch para evitar N+1 (por nome)
      Map<String, Long> categoryIdByName = new HashMap<>();
      Map<String, Long> subcategoryIdByName = new HashMap<>();

      if (!categoryNames.isEmpty()) {
        // Primeiro busca em categorias pelo nome
        List<CategoryEntity> categories = categoryRepository.findByClientConceptsCashFlowIdAndNames(
            cashFlowId, categoryNames);
        for (CategoryEntity cat : categories) {
          categoryIdByName.put(cat.getName(), cat.getId());
        }

        // Nomes não encontrados em categorias, buscar em subcategorias
        Set<String> notFoundInCategories = new HashSet<>(categoryNames);
        notFoundInCategories.removeAll(categoryIdByName.keySet());

        if (!notFoundInCategories.isEmpty()) {
          List<SubcategoryEntity> subcategories = subcategoryRepository.findByClientConceptsCashFlowIdAndNames(
              cashFlowId, notFoundInCategories);
          for (SubcategoryEntity sub : subcategories) {
            subcategoryIdByName.put(sub.getName(), sub.getId());
          }
        }
      }

      for (OpenFinanceTransaction transaction : transactions) {
        long subcents = Math.round(transaction.getAmount() * 100);
        LocalDateTime now = LocalDateTime.now();

        // Determinar categoryId e subcategoryId pelo nome
        Long categoryId = null;
        Long subcategoryId = null;
        String categoryName = transaction.getCategory();
        if (categoryName != null && !categoryName.isBlank()) {
          if (categoryIdByName.containsKey(categoryName)) {
            categoryId = categoryIdByName.get(categoryName);
          } else if (subcategoryIdByName.containsKey(categoryName)) {
            subcategoryId = subcategoryIdByName.get(categoryName);
          }
        }

        if (existingIds.contains(transaction.getId())) {
          TransactionEntity existing = transactionRepository.findAllByIntegrationIds(List.of(transaction.getId()))
              .stream().findFirst().orElseThrow();
          transactionRepository.update(
              "name = ?1, originalValueSubcents = ?2, tempValueSubcents = ?3, valueSubcents = ?4, transactionDate = ?5, updatedAt = ?6, clientConceptsCashFlowCategoryId = ?7, clientConceptsCashFlowSubcategoryId = ?8 WHERE id = ?9",
              transaction.getDescription() != null ? transaction.getDescription() : "",
              subcents,
              subcents,
              subcents,
              transaction.getDate() != null ? transaction.getDate().toLocalDate() : null,
              now,
              categoryId,
              subcategoryId,
              existing.getId());
          continue;
        }

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setCashFlow(cashFlow);
        transactionEntity.setName(transaction.getDescription() != null ? transaction.getDescription() : "");
        transactionEntity.setEntryMode("debit");
        transactionEntity.setOriginalValueSubcents(subcents);
        transactionEntity.setTempValueSubcents(subcents);
        transactionEntity.setValueSubcents(subcents);
        transactionEntity
            .setTransactionDate(transaction.getDate() != null ? transaction.getDate().toLocalDate() : null);
        transactionEntity.setTransactionType(transaction.getType());
        transactionEntity.setIntegrationId(transaction.getId());
        transactionEntity.setClientConceptsCashFlowCategoryId(categoryId);
        transactionEntity.setClientConceptsCashFlowPurchaseId(null);
        transactionEntity.setClientConceptsCashFlowSubcategoryId(subcategoryId);
        transactionEntity.setCreatedAt(now);
        transactionEntity.setUpdatedAt(now);
        transactionRepository.persist(transactionEntity);
      }
    }
  }

  private Set<String> getExistingTransactionIds(OpenFinanceTransaction[] transactions) {
    List<String> transactionIdsToCheck = Arrays.stream(transactions)
        .map(OpenFinanceTransaction::getId)
        .toList();

    return transactionRepository
        .findAllByIntegrationIds(transactionIdsToCheck)
        .stream()
        .map(TransactionEntity::getIntegrationId)
        .collect(Collectors.toSet());
  }
}
