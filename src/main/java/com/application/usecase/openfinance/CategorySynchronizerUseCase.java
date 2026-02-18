package com.application.usecase.openfinance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domain.entities.AccountEntity;
import com.domain.entities.CashFlowEntity;
import com.domain.entities.CategoryEntity;
import com.domain.entities.SubcategoryEntity;
import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.OpenFinanceCategory;
import com.domain.repositories.categories.ICategoryRepository;
import com.domain.repositories.subcategories.ISubcategoryRepository;
import com.domain.shared.PaginatedResponse;
import com.domain.usecase.cashflow.IUpsertCashFlowUseCase;
import com.domain.usecase.openfinance.ICategorySynchronizerUseCase;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CategorySynchronizerUseCase implements ICategorySynchronizerUseCase {

  private static final Logger LOG = Logger.getLogger(CategorySynchronizerUseCase.class);

  @Inject
  private IOpenFinance openFinance;

  @Inject
  private IUpsertCashFlowUseCase upsertCashFlowUseCase;

  @Inject
  private ICategoryRepository categoryRepository;

  @Inject
  private ISubcategoryRepository subcategoryRepository;

  @Override
  @Transactional
  public void synchronizeCategories(AccountEntity account) {
    CashFlowEntity cashFlow = upsertCashFlowUseCase.upsertCashFlow(account.getClientConceptId());
    Long cashFlowId = cashFlow.getId();

    CategoryLookup categoryLookup = loadCategoriesAndSubcategories(cashFlowId);

    Map<String, CategoryEntity> categoryMapByPluggyId = persistCategories(cashFlowId, categoryLookup.categories());
    persistSubcategories(cashFlowId, categoryLookup.subcategories(), categoryMapByPluggyId);
  }

  private List<OpenFinanceCategory> fetchAllCategoriesFromPluggy() {
    List<OpenFinanceCategory> all = new ArrayList<>();
    int page = 1;
    int totalPages = 1;
    do {
      PaginatedResponse<OpenFinanceCategory> response = openFinance.listCategories(page);
      totalPages = response.getTotalPages();
      OpenFinanceCategory[] items = response.getItems();
      if (items != null) {
        for (OpenFinanceCategory item : items) {
          all.add(item);
        }
      }
      page++;
    } while (page <= totalPages);
    return all;
  }

  private CategoryLookup loadCategoriesAndSubcategories(Long cashFlowId) {
    List<OpenFinanceCategory> allFromPluggy = fetchAllCategoriesFromPluggy();
    List<OpenFinanceCategory> withoutParent = new ArrayList<>();
    List<OpenFinanceCategory> withParent = new ArrayList<>();

    for (OpenFinanceCategory c : allFromPluggy) {
      if (c.getParentId() == null || c.getParentId().isBlank()) {
        withoutParent.add(c);
      } else {
        withParent.add(c);
      }
    }

    return new CategoryLookup(withoutParent, withParent);
  }

  private Map<String, CategoryEntity> persistCategories(Long cashFlowId, List<OpenFinanceCategory> categories) {
    Map<String, CategoryEntity> categoryMapByPluggyId = new HashMap<>();

    for (OpenFinanceCategory c : categories) {
      String name = c.getDescriptionTranslated() != null ? c.getDescriptionTranslated() : c.getDescription();
      String originalName = c.getDescription();
      String pluggyId = c.getId();
      CategoryEntity existing = categoryRepository.findByClientConceptsCashFlowIdAndPluggyId(cashFlowId, pluggyId);

      if (existing != null) {
        categoryMapByPluggyId.put(pluggyId, existing);
        LOG.infof("Category already exists: %s", name);
        continue;
      }

      CategoryEntity category = new CategoryEntity(name, originalName, cashFlowId, pluggyId);
      categoryRepository.persist(category);
      categoryMapByPluggyId.put(pluggyId, category);
      LOG.infof("Category created: %s (pluggy_id=%s)", name, pluggyId);
    }

    return categoryMapByPluggyId;
  }

  private void persistSubcategories(Long cashFlowId, List<OpenFinanceCategory> subcategories,
      Map<String, CategoryEntity> categoryMapByPluggyId) {

    LOG.infof("Persisting %d subcategories", subcategories.size());
    LOG.infof("Category map by pluggy id: %s", categoryMapByPluggyId);

    for (OpenFinanceCategory c : subcategories) {
      String parentPluggyId = c.getParentId();
      CategoryEntity parentCategory = categoryMapByPluggyId.get(parentPluggyId);

      if (parentCategory == null) {
        continue;
      }

      String name = c.getDescriptionTranslated() != null ? c.getDescriptionTranslated() : c.getDescription();
      String originalName = c.getDescription();
      String pluggyId = c.getId();
      SubcategoryEntity existing = subcategoryRepository.findByClientConceptsCashFlowIdAndPluggyId(cashFlowId,
          pluggyId);

      if (existing != null) {
        LOG.infof("Subcategory already exists: %s", name);
        continue;
      }

      SubcategoryEntity subcategory = new SubcategoryEntity(name, originalName, pluggyId, parentCategory, cashFlowId);
      subcategoryRepository.persist(subcategory);
      LOG.infof("Subcategory created: %s (pluggy_id=%s) -> category %s", name, pluggyId, parentCategory.getName());
    }
  }

  private record CategoryLookup(
      List<OpenFinanceCategory> categories,
      List<OpenFinanceCategory> subcategories) {
  }
}
