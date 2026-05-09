package com.expenses.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CategoryTypeGoalItemDto {
    private Long categoryTypeId;
    private String categoryTypeName;
    private BigDecimal targetAmount;
}
