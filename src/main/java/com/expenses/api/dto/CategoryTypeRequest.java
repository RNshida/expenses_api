package com.expenses.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryTypeRequest {

    @NotBlank(message = "種別名は必須です")
    @Size(max = 100, message = "種別名は100文字以内で入力してください")
    private String name;
}
