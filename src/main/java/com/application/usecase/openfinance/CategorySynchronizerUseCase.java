package com.application.usecase.openfinance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    List<OpenFinanceCategory> allFromPluggy = fetchAllCategoriesFromPluggy();

    // Separar em duas listas: sem parentId (categorias) e com parentId (subcategorias)
    List<OpenFinanceCategory> withoutParent = new ArrayList<>();
    List<OpenFinanceCategory> withParent = new ArrayList<>();

    for (OpenFinanceCategory c : allFromPluggy) {
      if (c.getParentId() == null || c.getParentId().isBlank()) {
        withoutParent.add(c);
      } else {
        withParent.add(c);
      }
    }

    // 1) Sincronizar categorias raiz (sem parentId): apenas salvar
    Map<String, CategoryEntity> categoryMapByPluggyId = new HashMap<>();
    for (OpenFinanceCategory c : withoutParent) {
      String name = extractName(c);
      String pluggyId = c.getId();
      CategoryEntity existing = categoryRepository.findByClientConceptsCashFlowIdAndPluggyId(cashFlowId, pluggyId);
      if (existing != null) {
        existing.setName(name);
        existing.setUpdatedAt(LocalDateTime.now());
        categoryMapByPluggyId.put(pluggyId, existing);
        LOG.infof("Category updated: %s", name);
      } else {
        CategoryEntity category = new CategoryEntity(name, cashFlowId, pluggyId);
        categoryRepository.persist(category);
        categoryMapByPluggyId.put(pluggyId, category);
        LOG.infof("Category created: %s (pluggy_id=%s)", name, pluggyId);
      }
    }

    // 2) Para subcategorias, coletar todos os parentIds únicos
    Set<String> parentPluggyIds = new HashSet<>();
    for (OpenFinanceCategory c : withParent) {
      parentPluggyIds.add(c.getParentId());
    }

    // 3) Buscar todas as categorias pai de uma vez para evitar N+1
    List<CategoryEntity> parentCategories = categoryRepository.findByClientConceptsCashFlowIdAndPluggyIds(
        cashFlowId, parentPluggyIds);

    // 4) Montar mapa em memória para lookup rápido
    for (CategoryEntity cat : parentCategories) {
      categoryMapByPluggyId.put(cat.getPluggyId(), cat);
    }

    // 5) Sincronizar subcategorias (com parentId)
    for (OpenFinanceCategory c : withParent) {
      String parentPluggyId = c.getParentId();
      CategoryEntity parentCategory = categoryMapByPluggyId.get(parentPluggyId);

      // Se não existe o parent, criar a categoria pai
      if (parentCategory == null) {
        LOG.warnf("Parent category not found for pluggy_id=%s, creating placeholder category", parentPluggyId);
        CategoryEntity newParent = new CategoryEntity("Categoria " + parentPluggyId, cashFlowId, parentPluggyId);
        categoryRepository.persist(newParent);
        categoryMapByPluggyId.put(parentPluggyId, newParent);
        parentCategory = newParent;
        LOG.infof("Placeholder category created for pluggy_id=%s", parentPluggyId);
      }

      String name = extractName(c);
      String pluggyId = c.getId();
      SubcategoryEntity existing = subcategoryRepository.findByClientConceptsCashFlowIdAndPluggyId(cashFlowId,
          pluggyId);
      if (existing != null) {
        existing.setName(name);
        existing.setCategory(parentCategory);
        existing.setUpdatedAt(LocalDateTime.now());
        subcategoryRepository.update(existing);
        LOG.infof("Subcategory updated: %s", name);
      } else {
        SubcategoryEntity subcategory = new SubcategoryEntity(name, pluggyId, parentCategory, cashFlowId);
        subcategoryRepository.persist(subcategory);
        LOG.infof("Subcategory created: %s (pluggy_id=%s) -> category %s", name, pluggyId, parentCategory.getName());
      }
    }
  }

  private String extractName(OpenFinanceCategory c) {
    String name = c.getDescriptionTranslated() != null ? c.getDescriptionTranslated() : c.getDescription();
    return name != null ? name : "";
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
}
