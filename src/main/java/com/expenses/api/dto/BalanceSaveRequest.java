package com.expenses.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class BalanceSaveRequest {

    @NotBlank
    @Pattern(regexp = "\\d{4}-\\d{2}", message = "年月はYYYY-MM形式で入力してください")
    private String yearMonth;

    @NotNull
    @Valid
    private List<BalanceItem> balances;

    @Getter
    @NoArgsConstructor
    public static class BalanceItem {
        @NotNull
        private Long categoryId;

        private BigDecimal amount;

        private String memo;
    }
}
