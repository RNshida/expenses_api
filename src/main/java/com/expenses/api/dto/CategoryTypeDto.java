package com.expenses.api.dto;

import com.expenses.api.entity.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryTypeDto {
    private Long id;
    private String name;
    private Integer displayOrder;

    public static CategoryTypeDto from(CategoryType type) {
        return new CategoryTypeDto(type.getId(), type.getName(), type.getDisplayOrder());
    }
}
