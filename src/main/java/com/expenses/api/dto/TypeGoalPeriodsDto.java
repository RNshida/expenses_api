package com.expenses.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class TypeGoalPeriodsDto {
    private Long categoryTypeId;
    private String categoryTypeName;
    private List<PeriodItem> periods;

    @Getter
    @AllArgsConstructor
    public static class PeriodItem {
        private Long id;
        private String startYearMonth;
        private String endYearMonth;
        private BigDecimal targetAmount;
    }
}
