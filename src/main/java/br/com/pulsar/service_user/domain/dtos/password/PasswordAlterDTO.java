package br.com.pulsar.service_user.domain.dtos.password;

import jakarta.validation.constraints.NotBlank;

public record PasswordAlterDTO(
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "New password is required")
        String newPassword,
        @NotBlank(message = "Confirm password is required")
        String confirmPassword
) {
}
