package com.domain.usecase.categories;

import java.util.Collection;
import java.util.List;

import com.domain.entities.CategoryEntity;

public interface ICategoryLookupUseCase {

  List<CategoryEntity> findByIds(Collection<Long> ids);

  List<CategoryEntity> findByNames(Long cashFlowId, Collection<String> names);
}
