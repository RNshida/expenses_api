package com.expenses.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GoalItemDto {
    private Long categoryId;
    private String categoryName;
    private BigDecimal targetAmount;
}
