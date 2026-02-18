package com.application.usecase.transactions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.application.dto.TransactionCategoryDTO;
import com.application.dto.TransactionDTO;
import com.domain.entities.CashFlowEntity;
import com.domain.entities.CategoryEntity;
import com.domain.entities.SubcategoryEntity;
import com.domain.entities.TransactionEntity;
import com.domain.repositories.transactions.ITransactionRepository;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.cashflow.IGetCashFlowByConceptUseCase;
import com.domain.usecase.categories.ICategoryLookupUseCase;
import com.domain.usecase.subcategories.ISubcategoryLookupUseCase;
import com.domain.usecase.transactions.ITransactionListUseCase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransactionsListUseCase implements ITransactionListUseCase {
  @Inject
  private ITransactionRepository transactionRepository;

  @Inject
  private IGetCashFlowByConceptUseCase getCashFlowByConceptUseCase;

  @Inject
  private ICategoryLookupUseCase categoryLookupUseCase;

  @Inject
  private ISubcategoryLookupUseCase subcategoryLookupUseCase;

  @Override
  public PaginatedResponse<TransactionDTO> listTransactions(Long conceptId) {
    return listTransactions(conceptId, 1);
  }

  @Override
  public PaginatedResponse<TransactionDTO> listTransactions(Long conceptId, Integer page) {
    return listTransactions(conceptId, page, 10);
  }

  @Override
  public PaginatedResponse<TransactionDTO> listTransactions(Long conceptId, Integer page, Integer pageSize) {
    CashFlowEntity cashFlow = getCashFlowByConceptUseCase.getCashFlowByConcept(conceptId);

    if (cashFlow == null) {
      return new PaginatedResponse<>(page, pageSize, 0, 0, new TransactionDTO[0]);
    }

    List<TransactionEntity> entities = transactionRepository.findByCashFlowId(cashFlow.getId(), page, pageSize);
    long total = transactionRepository.countByCashFlowId(cashFlow.getId());
    int totalPages = (int) Math.ceil((double) total / pageSize);

    Map<Long, TransactionCategoryDTO> categoriesById = loadCategories(entities);
    Map<Long, TransactionCategoryDTO> subcategoriesById = loadSubcategories(entities);

    TransactionDTO[] dtos = entities.stream()
        .map(entity -> {
          TransactionDTO dto = TransactionDTO.fromEntity(entity);
          if (entity.getClientConceptsCashFlowCategoryId() != null) {
            dto.setCategory(categoriesById.get(entity.getClientConceptsCashFlowCategoryId()));
          }
          if (entity.getClientConceptsCashFlowSubcategoryId() != null) {
            dto.setSubcategory(subcategoriesById.get(entity.getClientConceptsCashFlowSubcategoryId()));
          }
          return dto;
        })
        .toArray(TransactionDTO[]::new);

    return new PaginatedResponse<>(page, pageSize, total, totalPages, dtos);
  }

  private Map<Long, TransactionCategoryDTO> loadCategories(List<TransactionEntity> entities) {
    Set<Long> categoryIds = entities.stream()
        .map(TransactionEntity::getClientConceptsCashFlowCategoryId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    if (categoryIds.isEmpty()) {
      return new HashMap<>();
    }

    List<CategoryEntity> categories = categoryLookupUseCase.findByIds(categoryIds);
    return categories.stream()
        .collect(Collectors.toMap(
            CategoryEntity::getId,
            TransactionCategoryDTO::fromCategory));
  }

  private Map<Long, TransactionCategoryDTO> loadSubcategories(List<TransactionEntity> entities) {
    Set<Long> subcategoryIds = entities.stream()
        .map(TransactionEntity::getClientConceptsCashFlowSubcategoryId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    if (subcategoryIds.isEmpty()) {
      return new HashMap<>();
    }

    List<SubcategoryEntity> subcategories = subcategoryLookupUseCase.findByIds(subcategoryIds);
    return subcategories.stream()
        .collect(Collectors.toMap(
            SubcategoryEntity::getId,
            TransactionCategoryDTO::fromSubcategory));
  }
}
