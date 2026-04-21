package com.expenses.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class GoalSaveRequest {

    private List<GoalItem> goals;

    @Getter
    @NoArgsConstructor
    public static class GoalItem {
        private Long categoryId;
        private BigDecimal targetAmount;
    }
}
