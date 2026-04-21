package com.expenses.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BalanceSummaryResponse {
    private List<String> months;
    private List<BalanceSummarySeriesDto> series;
}
