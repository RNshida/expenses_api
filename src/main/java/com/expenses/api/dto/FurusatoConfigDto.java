package com.expenses.api.dto;

import com.expenses.api.entity.FurusatoConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class FurusatoConfigDto {

    private Integer annualIncome;
    private BigDecimal limitAmount;

    public static FurusatoConfigDto from(FurusatoConfig c) {
        return new FurusatoConfigDto(c.getAnnualIncome(), c.getLimitAmount());
    }

    public static FurusatoConfigDto empty() {
        return new FurusatoConfigDto(null, null);
    }
}
