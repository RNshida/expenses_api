package com.expenses.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class BalanceSummarySeriesDto {
    private Long categoryId;
    private String categoryName;
    private List<BigDecimal> amounts;
}
