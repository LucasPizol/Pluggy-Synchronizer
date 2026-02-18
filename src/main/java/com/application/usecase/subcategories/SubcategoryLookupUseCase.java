package com.application.usecase.subcategories;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.domain.entities.SubcategoryEntity;
import com.domain.repositories.subcategories.ISubcategoryRepository;
import com.domain.usecase.subcategories.ISubcategoryLookupUseCase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SubcategoryLookupUseCase implements ISubcategoryLookupUseCase {

  @Inject
  private ISubcategoryRepository subcategoryRepository;

  @Override
  public List<SubcategoryEntity> findByIds(Collection<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return Collections.emptyList();
    }
    return subcategoryRepository.findByIds(ids);
  }

  @Override
  public List<SubcategoryEntity> findByNames(Long cashFlowId, Collection<String> names) {
    if (names == null || names.isEmpty()) {
      return Collections.emptyList();
    }
    return subcategoryRepository.findByClientConceptsCashFlowIdAndNames(cashFlowId, names);
  }
}
