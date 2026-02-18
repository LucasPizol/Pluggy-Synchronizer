package com.domain.repositories.categories;

import java.util.Collection;
import java.util.List;

import com.domain.entities.CategoryEntity;

public interface ICategoryRepository {

  void persist(CategoryEntity entity);

  CategoryEntity findByClientConceptsCashFlowIdAndName(Long clientConceptsCashFlowId, String name);

  CategoryEntity findByClientConceptsCashFlowIdAndPluggyId(Long clientConceptsCashFlowId, String pluggyId);

  List<CategoryEntity> findByClientConceptsCashFlowIdAndPluggyIds(Long clientConceptsCashFlowId,
      Collection<String> pluggyIds);

  List<CategoryEntity> findByClientConceptsCashFlowIdAndNames(Long clientConceptsCashFlowId,
      Collection<String> names);

  List<CategoryEntity> findByIds(Collection<Long> ids);

  List<CategoryEntity> findByClientConceptsCashFlowId(Long clientConceptsCashFlowId);

  List<CategoryEntity> findByClientConceptsCashFlowId(Long clientConceptsCashFlowId, Integer page,
      Integer pageSize);

  long countByClientConceptsCashFlowId(Long clientConceptsCashFlowId);
}
