package com.application.usecase.categories;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.domain.entities.CategoryEntity;
import com.domain.repositories.categories.ICategoryRepository;
import com.domain.usecase.categories.ICategoryLookupUseCase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CategoryLookupUseCase implements ICategoryLookupUseCase {

  @Inject
  private ICategoryRepository categoryRepository;

  @Override
  public List<CategoryEntity> findByIds(Collection<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return Collections.emptyList();
    }
    return categoryRepository.findByIds(ids);
  }

  @Override
  public List<CategoryEntity> findByNames(Long cashFlowId, Collection<String> names) {
    if (names == null || names.isEmpty()) {
      return Collections.emptyList();
    }
    return categoryRepository.findByClientConceptsCashFlowIdAndNames(cashFlowId, names);
  }
}
