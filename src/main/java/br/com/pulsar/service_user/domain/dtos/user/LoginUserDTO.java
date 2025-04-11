package br.com.pulsar.service_user.domain.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserDTO(
        @Email(message = "Email is required.")
        String email,
        @NotBlank(message = "Password is required.")
        String password
) {
}
