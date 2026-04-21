package com.expenses.api.dto;

import com.expenses.api.entity.FurusatoContribution;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class FurusatoContributionDto {

    private Long id;
    private Integer fiscalYear;
    private String municipality;
    private String productName;
    private BigDecimal amount;
    private String status;

    public static FurusatoContributionDto from(FurusatoContribution c) {
        return new FurusatoContributionDto(
                c.getId(),
                c.getFiscalYear(),
                c.getMunicipality(),
                c.getProductName(),
                c.getAmount(),
                c.getStatus()
        );
    }
}
