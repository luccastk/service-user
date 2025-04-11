package br.com.pulsar.service_user.domain.dtos.http;

import br.com.pulsar.service_user.domain.models.Role;

import java.util.List;
import java.util.UUID;

public record ResponseUserDTO(
        UUID id,
        String name,
        String position,
        String departament,
        String email,
        List<Role> roleName
) {
}
