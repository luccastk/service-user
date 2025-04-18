package br.com.pulsar.service_user.controllers;

import br.com.pulsar.service_user.domain.dtos.jwt.RecoveryJwtTokenDTO;
import br.com.pulsar.service_user.domain.dtos.password.PasswordAlterDTO;
import br.com.pulsar.service_user.domain.dtos.user.LoginUserDTO;
import br.com.pulsar.service_user.domain.presenters.DataPresenter;
import br.com.pulsar.service_user.domain.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<DataPresenter<RecoveryJwtTokenDTO>> authenticatorUser(
            @RequestBody LoginUserDTO login
    ) {
        return ResponseEntity.ok(new DataPresenter<>(userService.authenticatorUser(login)));
    }

    @PutMapping("/password/change")
    public ResponseEntity<?> changePassword(
            @RequestBody PasswordAlterDTO dto
    ) {
        userService.passwordAlter(dto);
        return ResponseEntity.ok().build();
    }
}
