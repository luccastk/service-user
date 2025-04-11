package br.com.pulsar.service_user.domain.services.employee;

import br.com.pulsar.service_user.domain.dtos.employee.CreateEmployee;
import br.com.pulsar.service_user.domain.dtos.http.ResponseUserDTO;
import br.com.pulsar.service_user.domain.mapper.EmployeeMapper;
import br.com.pulsar.service_user.domain.repositories.EmployeeRepository;
import br.com.pulsar.service_user.domain.repositories.UserRepository;
import br.com.pulsar.service_user.domain.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ResponseUserDTO create(CreateEmployee json) {

        if (userRepository.existsByNameIgnoreCase(json.user().name())) {
            throw new DuplicateKeyException("User already register.");
        }

        userService.saveUser(json.user());

        return employeeMapper.toResponseDTO(
                employeeRepository.save(
                        employeeMapper.toEntity(json)
                )
        );
    }
}
