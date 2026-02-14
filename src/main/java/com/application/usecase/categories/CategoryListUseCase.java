package com.application.usecase.categories;

import com.application.dto.CategoryDTO;
import com.domain.entities.CashFlowEntity;
import com.domain.repositories.categories.ICategoryRepository;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.cashflow.IGetCashFlowByConceptUseCase;
import com.domain.usecase.categories.ICategoryListUseCase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CategoryListUseCase implements ICategoryListUseCase {

  @Inject
  private IGetCashFlowByConceptUseCase getCashFlowByConceptUseCase;

  @Inject
  private ICategoryRepository categoryRepository;

  @Override
  public PaginatedResponse<CategoryDTO> listCategories(Long conceptId, Integer page, Integer pageSize) {
    CashFlowEntity cashFlow = getCashFlowByConceptUseCase.getCashFlowByConcept(conceptId);

    if (cashFlow == null) {
      return new PaginatedResponse<>(page, pageSize, 0, 0, new CategoryDTO[0]);
    }

    Long cashFlowId = cashFlow.getId();
    var entities = categoryRepository.findByClientConceptsCashFlowId(cashFlowId, page, pageSize);
    long total = categoryRepository.countByClientConceptsCashFlowId(cashFlowId);
    int totalPages = (int) Math.ceil((double) total / pageSize);

    CategoryDTO[] dtos = entities.stream()
        .map(CategoryDTO::fromEntity)
        .toArray(CategoryDTO[]::new);

    return new PaginatedResponse<>(page, pageSize, total, totalPages, dtos);
  }
}
