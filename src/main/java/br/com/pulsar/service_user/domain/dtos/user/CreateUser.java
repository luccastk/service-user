package br.com.pulsar.service_user.domain.dtos.user;

import jakarta.validation.constraints.NotBlank;

public record CreateUser(
        @NotBlank(message = "Name is required.")
        String name,
        @NotBlank(message = "Email is required.")
        String email,
        @NotBlank(message = "Role is required.")
        String role
) {
}
