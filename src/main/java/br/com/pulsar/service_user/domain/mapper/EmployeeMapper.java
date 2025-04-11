package br.com.pulsar.service_user.domain.mapper;

import br.com.pulsar.service_user.domain.dtos.employee.CreateEmployee;
import br.com.pulsar.service_user.domain.dtos.http.ResponseUserDTO;
import br.com.pulsar.service_user.domain.models.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    private final UserMapper userMapper;

    public EmployeeMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Employee toEntity(CreateEmployee dto) {
        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setPosition(dto.position());
        employee.setDepartament(dto.departament());
        employee.setUser(userMapper.toEntity(dto.user()));

        return employee;
    }

    public ResponseUserDTO toResponseDTO(Employee entity) {
        return new ResponseUserDTO(
                entity.getId(),
                entity.getPosition(),
                entity.getDepartament(),
                entity.getUser().getName(),
                entity.getUser().getEmail(),
                entity.getUser().getRoles()
        );
    }
}
