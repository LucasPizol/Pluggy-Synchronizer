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
import com.domain.repositories.transactions.ITransactionRepository;
import com.domain.shared.MoneyEmbeddable;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.accountitem.IListAccountItemUseCase;
import com.domain.usecase.cashflow.IUpsertCashFlowUseCase;
import com.domain.usecase.categories.ICategoryLookupUseCase;
import com.domain.usecase.openfinance.ITransactionSynchronizerUseCase;
import com.domain.usecase.subcategories.ISubcategoryLookupUseCase;

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
  private ICategoryLookupUseCase categoryLookupUseCase;

  @Inject
  private ISubcategoryLookupUseCase subcategoryLookupUseCase;

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

      CategoryLookup categoryLookup = loadCategoriesAndSubcategories(cashFlowId, transactions);

      for (OpenFinanceTransaction transaction : transactions) {
        long subcents = Math.round(transaction.getAmount() * 100);
        LocalDateTime now = LocalDateTime.now();

        Long categoryId = findCategory(transaction, categoryLookup);
        Long subcategoryId = findSubcategory(transaction, categoryId, categoryLookup);

        LOG.infof("Category ID: %s", categoryId);
        LOG.infof("Subcategory ID: %s", subcategoryId);

        if (existingIds.contains(transaction.getId())) {
          TransactionEntity existing = transactionRepository.findAllByIntegrationIds(List.of(transaction.getId()))
              .stream().findFirst().orElseThrow();
          transactionRepository.update(
              "name = ?1, originalValue.valueCents = ?2, tempValue.valueCents = ?3, value.valueCents = ?4, transactionDate = ?5, updatedAt = ?6, clientConceptsCashFlowCategoryId = ?7, clientConceptsCashFlowSubcategoryId = ?8 WHERE id = ?9",
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
        transactionEntity.setOriginalValue(new MoneyEmbeddable(subcents, "br"));
        transactionEntity.setTempValue(new MoneyEmbeddable(subcents, "br"));
        transactionEntity.setValue(new MoneyEmbeddable(subcents, "br"));
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

  private CategoryLookup loadCategoriesAndSubcategories(Long cashFlowId, OpenFinanceTransaction[] transactions) {
    Set<String> categoryNames = Arrays.stream(transactions)
        .map(OpenFinanceTransaction::getCategory)
        .filter(c -> c != null && !c.isBlank())
        .collect(Collectors.toSet());

    Map<String, Long> categoryIdByName = new HashMap<>();
    Map<String, Long> subcategoryIdByName = new HashMap<>();
    Map<String, Long> subcategoryCategoryIdByName = new HashMap<>();

    if (!categoryNames.isEmpty()) {
      List<CategoryEntity> categories = categoryLookupUseCase.findByNames(cashFlowId, categoryNames);

      LOG.infof("Categories: %s", categories);

      for (CategoryEntity cat : categories) {
        categoryIdByName.put(cat.getOriginalName().toLowerCase(), cat.getId());
      }

      Set<String> notFoundInCategories = new HashSet<>(categoryNames);
      notFoundInCategories.removeAll(categoryIdByName.keySet());

      if (!notFoundInCategories.isEmpty()) {
        List<SubcategoryEntity> subcategories = subcategoryLookupUseCase.findByNames(
            cashFlowId, notFoundInCategories);

        for (SubcategoryEntity sub : subcategories) {
          String nameLower = sub.getOriginalName().toLowerCase();
          subcategoryIdByName.put(nameLower, sub.getId());
          subcategoryCategoryIdByName.put(nameLower, sub.getCategory().getId());
        }
      }
    }

    return new CategoryLookup(categoryIdByName, subcategoryIdByName, subcategoryCategoryIdByName);
  }

  private Long findCategory(OpenFinanceTransaction transaction, CategoryLookup lookup) {
    String catName = transaction.getCategory();

    if (catName == null || catName.isBlank()) {
      return null;
    }

    LOG.infof("Category name: %s", catName);

    String categoryNameLower = catName.toLowerCase();

    if (lookup.categoryIdByName().containsKey(categoryNameLower)) {
      return lookup.categoryIdByName().get(categoryNameLower);
    }

    if (lookup.subcategoryCategoryIdByName().containsKey(categoryNameLower)) {
      return lookup.subcategoryCategoryIdByName().get(categoryNameLower);
    }

    return null;
  }

  private Long findSubcategory(OpenFinanceTransaction transaction, Long categoryId, CategoryLookup lookup) {
    if (categoryId == null) {
      return null;
    }

    String catName = transaction.getCategory();
    if (catName == null || catName.isBlank()) {
      return null;
    }

    String categoryNameLower = catName.toLowerCase();

    if (lookup.subcategoryIdByName().containsKey(categoryNameLower)) {
      return lookup.subcategoryIdByName().get(categoryNameLower);
    }

    return null;
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

  private record CategoryLookup(
      Map<String, Long> categoryIdByName,
      Map<String, Long> subcategoryIdByName,
      Map<String, Long> subcategoryCategoryIdByName) {
  }
}
