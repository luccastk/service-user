package br.com.pulsar.service_user.domain.dtos.employee;

import br.com.pulsar.service_user.domain.dtos.user.CreateUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEmployee(
        @NotBlank(message = "Position is required.")
        String position,
        @NotBlank(message = "Departament is required.")
        String departament,
        @NotNull
        CreateUser user
) {
}
