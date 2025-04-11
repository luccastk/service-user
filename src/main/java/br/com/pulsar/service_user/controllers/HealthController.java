package br.com.pulsar.service_user.controllers;

import br.com.pulsar.service_user.domain.dtos.IndexResponseDTO;
import br.com.pulsar.service_user.domain.presenters.DataPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthController {

    @GetMapping
    public ResponseEntity<DataPresenter<IndexResponseDTO>> getHealth() {
        return ResponseEntity.ok(new DataPresenter<>(new IndexResponseDTO()));
    }
}
