package br.com.pulsar.service_user.domain.services.user;

import br.com.pulsar.service_user.domain.dtos.jwt.RecoveryJwtTokenDTO;
import br.com.pulsar.service_user.domain.dtos.kafka.PasswordChangeKafkaEvent;
import br.com.pulsar.service_user.domain.dtos.password.PasswordAlterDTO;
import br.com.pulsar.service_user.domain.dtos.user.CreateUser;
import br.com.pulsar.service_user.domain.dtos.user.LoginUserDTO;
import br.com.pulsar.service_user.domain.mapper.UserMapper;
import br.com.pulsar.service_user.domain.models.Role;
import br.com.pulsar.service_user.domain.models.User;
import br.com.pulsar.service_user.domain.repositories.RoleRepository;
import br.com.pulsar.service_user.domain.repositories.UserRepository;
import br.com.pulsar.service_user.domain.services.kafka.MailEventPublisher;
import br.com.pulsar.service_user.exception.PasswordNotMatchException;
import br.com.pulsar.service_user.infra.jwt.JwtTokenService;
import br.com.pulsar.service_user.utils.PasswordGenerator;
import br.com.pulsar.service_user.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MailEventPublisher mailEventPublisher;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public RecoveryJwtTokenDTO authenticatorUser(LoginUserDTO login) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(login.email(), login.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new RecoveryJwtTokenDTO(jwtTokenService.generateToken(userDetails));
    }

    @Transactional
    public User saveUser(CreateUser json) {
        User user = userMapper.toEntity(json);

        mailEventPublisher.sendWelcomeMail(userMapper.toKafkaUser(json), user);

        String temporaryPassword = generatePassword();

        mailEventPublisher.sendPasswordChange(
                userMapper.toKafkaUserAndPassword(json, temporaryPassword), user
        );

        user.setPassword(passwordEncoder.encode(
                temporaryPassword
        ));
        user.setRoles(getRoleName(json));

        return userRepository.save(user);
    }

    @Transactional
    public void passwordAlter(PasswordAlterDTO password) {
        User user = userRepository.findByEmailIgnoreCase(SecurityUtils.getCurrentEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        if (!passwordEncoder.matches(password.password(), user.getPassword())) {
            throw new PasswordNotMatchException("Password dosen't match");
        }

        if (!password.newPassword().equals(password.confirmPassword())) {
            throw new PasswordNotMatchException("New password doesn't match");
        }

        user.setPassword(
                passwordEncoder.encode(password.newPassword())
        );
        userRepository.save(user);
    }

    @Transactional
    private List<Role> getRoleName(CreateUser json) {
        return json.role().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() ->  new IllegalArgumentException("Role not found: " + roleName)))
                .toList();
    }

    private String generatePassword(){
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();

        return passwordGenerator.generate(10);
    }
}
