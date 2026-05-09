package com.expenses.api.service;

import com.expenses.api.dto.CategoryTypeDto;
import com.expenses.api.dto.CategoryTypeReorderRequest;
import com.expenses.api.dto.CategoryTypeRequest;
import com.expenses.api.entity.AppUser;
import com.expenses.api.entity.CategoryType;
import com.expenses.api.repository.CategoryTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryTypeService {

    private final CategoryTypeRepository categoryTypeRepository;

    @Transactional(readOnly = true)
    public List<CategoryTypeDto> getAll(AppUser user) {
        return categoryTypeRepository.findByUserOrderByDisplayOrderAscNameAsc(user)
                .stream()
                .map(CategoryTypeDto::from)
                .toList();
    }

    @Transactional
    public CategoryTypeDto add(AppUser user, CategoryTypeRequest request) {
        if (categoryTypeRepository.existsByUserAndName(user, request.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "種別名「" + request.getName() + "」は既に存在します");
        }
        int nextOrder = categoryTypeRepository.findByUserOrderByDisplayOrderAscNameAsc(user).size();
        CategoryType type = new CategoryType(user, request.getName(), nextOrder);
        return CategoryTypeDto.from(categoryTypeRepository.save(type));
    }

    @Transactional
    public CategoryTypeDto update(AppUser user, Long id, CategoryTypeRequest request) {
        CategoryType type = categoryTypeRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "種別が見つかりません"));
        if (categoryTypeRepository.existsByUserAndNameAndIdNot(user, request.getName(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "種別名「" + request.getName() + "」は既に存在します");
        }
        type.setName(request.getName());
        return CategoryTypeDto.from(categoryTypeRepository.save(type));
    }

    @Transactional
    public void reorder(AppUser user, CategoryTypeReorderRequest request) {
        for (CategoryTypeReorderRequest.CategoryTypeOrderItem item : request.getOrders()) {
            CategoryType type = categoryTypeRepository.findByIdAndUser(item.getId(), user)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "種別が見つかりません"));
            type.setDisplayOrder(item.getDisplayOrder());
            categoryTypeRepository.save(type);
        }
    }

    @Transactional
    public void delete(AppUser user, Long id) {
        CategoryType type = categoryTypeRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "種別が見つかりません"));
        categoryTypeRepository.delete(type);
    }
}
