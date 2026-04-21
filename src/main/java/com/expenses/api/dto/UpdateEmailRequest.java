package com.expenses.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateEmailRequest {

    @NotBlank
    @Email
    private String newEmail;

    @NotBlank
    private String currentPassword;
}
