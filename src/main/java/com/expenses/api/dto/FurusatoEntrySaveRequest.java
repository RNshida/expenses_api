package com.expenses.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class FurusatoEntrySaveRequest {

    @NotBlank
    @Size(max = 50)
    private String municipality;

    @Size(max = 20)
    private String productName;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    @Pattern(regexp = "未申請|ワンストップ申請済み|確定申告予定|確定申告済み")
    private String status;
}
