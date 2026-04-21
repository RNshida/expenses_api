package com.expenses.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class FurusatoConfigSaveRequest {

    private Integer annualIncome;
    private BigDecimal limitAmount;
}
