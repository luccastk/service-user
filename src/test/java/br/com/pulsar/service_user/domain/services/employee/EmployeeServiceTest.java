package br.com.pulsar.service_user.domain.services.employee;

import br.com.pulsar.service_user.domain.dtos.employee.CreateEmployee;
import br.com.pulsar.service_user.domain.dtos.employee.EmployeeWrapperDTO;
import br.com.pulsar.service_user.domain.dtos.http.ResponseEmployeeDTO;
import br.com.pulsar.service_user.domain.dtos.user.CreateUser;
import br.com.pulsar.service_user.domain.mapper.EmployeeMapper;
import br.com.pulsar.service_user.domain.models.Employee;
import br.com.pulsar.service_user.domain.models.User;
import br.com.pulsar.service_user.domain.repositories.EmployeeRepository;
import br.com.pulsar.service_user.domain.repositories.UserRepository;
import br.com.pulsar.service_user.domain.services.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private EmployeeService employeeService;

    private CreateEmployee createEmployee;
    private CreateUser createUser;
    private User user;
    private Employee employee;
    private ResponseEmployeeDTO responseEmployeeDTO;

    @BeforeEach
    void setup() {
        createUser = new CreateUser("Test", "test@email.com", List.of());
        createEmployee = new CreateEmployee(
                "Seller",
                "Store test",
                createUser
        );

        user = new User();
        employee = new Employee();
        employee.setUser(user);

        responseEmployeeDTO = mock(ResponseEmployeeDTO.class);
    }

    @Test
    void shouldCreateEmployeeSuccessfully() {
        when(userRepository.existsByNameIgnoreCase(createUser.name())).thenReturn(false);
        when(userRepository.existsByEmailIgnoreCase(createUser.email())).thenReturn(false);
        when(userService.saveUser(createUser)).thenReturn(user);
        when(employeeMapper.toEntity(createEmployee)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponseDTO(employee)).thenReturn(responseEmployeeDTO);

        ResponseEmployeeDTO result = employeeService.create(createEmployee);

        assertEquals(responseEmployeeDTO, result);
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldThrowExceptionIfUserNameExists() {
        when(userRepository.existsByNameIgnoreCase(createUser.name())).thenReturn(true);
        assertThrows(DuplicateKeyException.class, () -> employeeService.create(createEmployee));
    }

    @Test
    void shouldThrowExceptionIfUserEmailExists() {
        when(userRepository.existsByNameIgnoreCase(createUser.name())).thenReturn(false);
        when(userRepository.existsByEmailIgnoreCase(createUser.email())).thenReturn(true);
        assertThrows(DuplicateKeyException.class, () -> employeeService.create(createEmployee));
    }

    @Test
    void shouldReturnListOfEmployees() {
        List<Employee> employees = List.of(new Employee());
        EmployeeWrapperDTO wrapperDTO = mock(EmployeeWrapperDTO.class);

        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.toWrapperDTO(employees)).thenReturn(wrapperDTO);

        EmployeeWrapperDTO result = employeeService.listEmployees();

        assertEquals(wrapperDTO, result);
    }

    @Test
    void shouldDeleteEmployeeSuccessfully() {
        UUID id = UUID.randomUUID();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        employeeService.delete(id);

        verify(employeeRepository).delete(employee);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentEmployee() {
        UUID id = UUID.randomUUID();
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> employeeService.delete(id));
    }
}