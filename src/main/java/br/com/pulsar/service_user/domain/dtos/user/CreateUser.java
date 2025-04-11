package br.com.pulsar.service_user.domain.dtos.user;

import br.com.pulsar.service_user.domain.models.Role;
import br.com.pulsar.service_user.domain.models.enums.RoleName;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateUser(
        @NotBlank(message = "Name is required.")
        String name,
        @NotBlank(message = "Email is required.")
        String email,
        @NotBlank(message = "Role is required.")
        List<RoleName> role
) {
}
