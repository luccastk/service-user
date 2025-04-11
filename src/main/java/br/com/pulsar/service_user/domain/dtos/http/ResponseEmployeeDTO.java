package br.com.pulsar.service_user.domain.dtos.http;

import br.com.pulsar.service_user.domain.models.enums.RoleName;

import java.util.List;
import java.util.UUID;

public record ResponseEmployeeDTO(
        UUID id,
        String name,
        String position,
        String departament,
        String email,
        List<RoleName> role
) {
}
