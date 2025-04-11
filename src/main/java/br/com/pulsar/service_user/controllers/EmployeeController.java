package br.com.pulsar.service_user.controllers;

import br.com.pulsar.service_user.domain.dtos.employee.CreateEmployee;
import br.com.pulsar.service_user.domain.dtos.http.ResponseUserDTO;
import br.com.pulsar.service_user.domain.presenters.DataPresenter;
import br.com.pulsar.service_user.domain.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-user/v1/admin")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/create")
    public ResponseEntity<DataPresenter<ResponseUserDTO>> createEmploye(
            @RequestBody CreateEmployee dto
            ) {
        return ResponseEntity.ok(new DataPresenter<>(employeeService.create(dto)));
    }
}
