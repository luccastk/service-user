package br.com.pulsar.service_user.controllers;

import br.com.pulsar.service_user.domain.dtos.employee.CreateEmployee;
import br.com.pulsar.service_user.domain.dtos.employee.EmployeeWrapperDTO;
import br.com.pulsar.service_user.domain.dtos.http.ResponseEmployeeDTO;
import br.com.pulsar.service_user.domain.presenters.DataPresenter;
import br.com.pulsar.service_user.domain.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/create")
    public ResponseEntity<DataPresenter<ResponseEmployeeDTO>> createEmploye(
            @RequestBody CreateEmployee dto
            ) {
        return ResponseEntity.ok(new DataPresenter<>(employeeService.create(dto)));
    }

    @GetMapping
    public ResponseEntity<DataPresenter<EmployeeWrapperDTO>> listEmployees() {
        return ResponseEntity.ok(new DataPresenter<>(employeeService.listEmployees()));
    }

    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<?> deleteEmployee(
            @PathVariable UUID employeeId
            ) {
        employeeService.delete(employeeId);
        return ResponseEntity.ok().build();
    }
}
