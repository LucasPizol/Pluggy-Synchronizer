package com.domain.usecase.categories;

import com.application.dto.CategoryDTO;
import com.domain.shared.PaginatedResponse;

public interface ICategoryListUseCase {

  PaginatedResponse<CategoryDTO> listCategories(Long conceptId, Integer page, Integer pageSize);
}
