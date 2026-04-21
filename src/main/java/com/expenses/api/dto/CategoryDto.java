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

    public static CategoryDto from(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getDisplayOrder());
    }
}
