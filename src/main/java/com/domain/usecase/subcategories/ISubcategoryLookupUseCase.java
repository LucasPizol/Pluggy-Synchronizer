package com.domain.usecase.subcategories;

import java.util.Collection;
import java.util.List;

import com.domain.entities.SubcategoryEntity;

public interface ISubcategoryLookupUseCase {

  List<SubcategoryEntity> findByIds(Collection<Long> ids);

  List<SubcategoryEntity> findByNames(Long cashFlowId, Collection<String> names);
}
