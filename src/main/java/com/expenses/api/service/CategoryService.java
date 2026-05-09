package com.expenses.api.service;

import com.expenses.api.dto.CategoryDto;
import com.expenses.api.dto.CategoryReorderRequest;
import com.expenses.api.dto.CategoryRequest;
import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.Category;
import com.expenses.api.entity.CategoryType;
import com.expenses.api.repository.CategoryGoalRepository;
import com.expenses.api.repository.CategoryRepository;
import com.expenses.api.repository.CategoryTypeRepository;
import com.expenses.api.repository.MonthlyBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final MonthlyBalanceRepository monthlyBalanceRepository;
    private final CategoryGoalRepository categoryGoalRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll(AppUser user) {
        return categoryRepository.findByUserOrderByDisplayOrderAscNameAsc(user)
                .stream()
                .map(CategoryDto::from)
                .toList();
    }

    @Transactional
    public CategoryDto add(AppUser user, CategoryRequest request) {
        if (categoryRepository.existsByUserAndName(user, request.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "カテゴリ名「" + request.getName() + "」は既に存在します");
        }
        int nextOrder = categoryRepository.findByUserOrderByDisplayOrderAscNameAsc(user).size();
        Category category = new Category(user, request.getName(), nextOrder);
        category.setColor(request.getColor());
        category.setCategoryType(resolveType(user, request.getCategoryTypeId()));
        return CategoryDto.from(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDto update(AppUser user, Long id, CategoryRequest request) {
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "カテゴリが見つかりません"));
        if (categoryRepository.existsByUserAndNameAndIdNot(user, request.getName(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "カテゴリ名「" + request.getName() + "」は既に存在します");
        }
        category.setName(request.getName());
        category.setColor(request.getColor());
        category.setCategoryType(resolveType(user, request.getCategoryTypeId()));
        return CategoryDto.from(categoryRepository.save(category));
    }

    @Transactional
    public void reorder(AppUser user, CategoryReorderRequest request) {
        for (CategoryReorderRequest.CategoryOrderItem item : request.getOrders()) {
            Category category = categoryRepository.findByIdAndUser(item.getId(), user)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "カテゴリが見つかりません"));
            category.setDisplayOrder(item.getDisplayOrder());
            categoryRepository.save(category);
        }
    }

    @Transactional
    public void delete(AppUser user, Long id) {
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "カテゴリが見つかりません"));
        monthlyBalanceRepository.deleteByCategoryId(id);
        categoryGoalRepository.deleteByUserAndCategoryId(user, id);
        categoryRepository.delete(category);
    }

    private CategoryType resolveType(AppUser user, Long typeId) {
        if (typeId == null) return null;
        return categoryTypeRepository.findByIdAndUser(typeId, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "指定された種別が見つかりません"));
    }
}
