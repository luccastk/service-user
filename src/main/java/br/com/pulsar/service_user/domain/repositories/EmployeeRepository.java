package br.com.pulsar.service_user.domain.repositories;

import br.com.pulsar.service_user.domain.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
