package br.com.pulsar.service_user.domain.mapper;

import br.com.pulsar.service_user.domain.dtos.employee.CreateEmployee;
import br.com.pulsar.service_user.domain.dtos.employee.EmployeeWrapperDTO;
import br.com.pulsar.service_user.domain.dtos.http.ResponseEmployeeDTO;
import br.com.pulsar.service_user.domain.models.Employee;
import br.com.pulsar.service_user.domain.models.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public ResponseEmployeeDTO toResponseDTO(Employee entity) {
        return new ResponseEmployeeDTO(
                entity.getId(),
                entity.getPosition(),
                entity.getDepartament(),
                entity.getUser().getName(),
                entity.getUser().getEmail(),
                entity.getUser().getRoles().stream().map(Role::getName).toList()
        );
    }

    public List<ResponseEmployeeDTO> toResponseDTO(List<Employee> entity) {
        if (entity == null) {
            return null;
        }

        List<ResponseEmployeeDTO> list = new ArrayList<ResponseEmployeeDTO>(entity.size());

        for ( Employee employee : entity ){
            list.add(toResponseDTO(employee));
        }

        return list;
    }

    public EmployeeWrapperDTO toWrapperDTO(List<Employee> entity) {
        return new EmployeeWrapperDTO(
                toResponseDTO(entity)
        );
    }
}
