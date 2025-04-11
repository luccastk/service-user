package br.com.pulsar.service_user.domain.dtos.employee;

import br.com.pulsar.service_user.domain.dtos.http.ResponseEmployeeDTO;

import java.util.List;

public record EmployeeWrapperDTO(
        List<ResponseEmployeeDTO> employees
) {
}
