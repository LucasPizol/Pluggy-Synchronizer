package com.domain.repositories.subcategories;

import java.util.Collection;
import java.util.List;

import com.domain.entities.SubcategoryEntity;

public interface ISubcategoryRepository {

  void persist(SubcategoryEntity entity);

  void update(SubcategoryEntity entity);

  SubcategoryEntity findByClientConceptsCashFlowIdAndPluggyId(Long clientConceptsCashFlowId, String pluggyId);

  List<SubcategoryEntity> findByCategoryId(Long categoryId);

  List<SubcategoryEntity> findByCategoryIds(Collection<Long> categoryIds);

  List<SubcategoryEntity> findByIds(Collection<Long> ids);

  List<SubcategoryEntity> findByClientConceptsCashFlowId(Long clientConceptsCashFlowId);

  List<SubcategoryEntity> findByClientConceptsCashFlowIdAndPluggyIds(Long clientConceptsCashFlowId,
      Collection<String> pluggyIds);

  List<SubcategoryEntity> findByClientConceptsCashFlowIdAndNames(Long clientConceptsCashFlowId,
      Collection<String> names);
}
