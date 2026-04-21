package com.expenses.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "カテゴリ名は必須です")
    @Size(max = 100, message = "カテゴリ名は100文字以内で入力してください")
    private String name;

    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "カラーコードは#RRGGBBの形式で入力してください")
    private String color;
}
