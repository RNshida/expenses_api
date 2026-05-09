package com.expenses.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class TypeGoalPeriodSaveRequest {
    private List<PeriodItem> periods;

    @Getter
    @NoArgsConstructor
    public static class PeriodItem {
        private String startYearMonth;
        private String endYearMonth;
        private BigDecimal targetAmount;
    }
}
