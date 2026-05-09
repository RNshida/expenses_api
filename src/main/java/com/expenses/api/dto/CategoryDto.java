package com.expenses.api.dto;

import com.expenses.api.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String color;
    private Integer displayOrder;
    private Long categoryTypeId;
    private String categoryTypeName;

    public static CategoryDto from(Category category) {
        Long typeId = category.getCategoryType() != null ? category.getCategoryType().getId() : null;
        String typeName = category.getCategoryType() != null ? category.getCategoryType().getName() : null;
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getDisplayOrder(),
                typeId,
                typeName);
    }
}
