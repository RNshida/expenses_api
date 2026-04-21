package com.expenses.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceInputItemDto {
    private Long categoryId;
    private String categoryName;
    private BigDecimal amount;
    private String memo;
}
