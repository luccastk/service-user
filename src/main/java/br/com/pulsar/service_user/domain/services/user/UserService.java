package br.com.pulsar.service_user.domain.services.user;

import br.com.pulsar.service_user.domain.dtos.jwt.RecoveryJwtTokenDTO;
import br.com.pulsar.service_user.domain.dtos.user.CreateUser;
import br.com.pulsar.service_user.domain.dtos.user.LoginUserDTO;
import br.com.pulsar.service_user.domain.mapper.UserMapper;
import br.com.pulsar.service_user.domain.models.User;
import br.com.pulsar.service_user.domain.repositories.UserRepository;
import br.com.pulsar.service_user.infra.configs.SecurityConfigs;
import br.com.pulsar.service_user.infra.jwt.JwtTokenService;
import br.com.pulsar.service_user.utils.PasswordGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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

    public User saveUser(CreateUser json) {
        User user = userMapper.toEntity(json);
        user.setPassword(
                passwordEncoder.encode(generatePassword())
        );
        return userRepository.save(user);
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
