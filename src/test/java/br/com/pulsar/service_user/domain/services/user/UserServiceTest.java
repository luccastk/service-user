package br.com.pulsar.service_user.domain.services.user;

import br.com.pulsar.service_user.domain.dtos.jwt.RecoveryJwtTokenDTO;
import br.com.pulsar.service_user.domain.dtos.password.PasswordAlterDTO;
import br.com.pulsar.service_user.domain.dtos.user.CreateUser;
import br.com.pulsar.service_user.domain.dtos.user.LoginUserDTO;
import br.com.pulsar.service_user.domain.mapper.UserMapper;
import br.com.pulsar.service_user.domain.models.Role;
import br.com.pulsar.service_user.domain.models.User;
import br.com.pulsar.service_user.domain.models.enums.RoleName;
import br.com.pulsar.service_user.domain.repositories.RoleRepository;
import br.com.pulsar.service_user.domain.repositories.UserRepository;
import br.com.pulsar.service_user.infra.kafka.MailEventPublisher;
import br.com.pulsar.service_user.exception.PasswordNotMatchException;
import br.com.pulsar.service_user.infra.jwt.JwtTokenService;
import br.com.pulsar.service_user.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private MailEventPublisher mailEventPublisher;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication auth;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldAuthenticatorUser() {
        LoginUserDTO login = new LoginUserDTO("user@example.com", "123456");
        String fakeJwt = "ey.fake.jwt.token";

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(userDetails);

        when(jwtTokenService.generateToken(userDetails))
                .thenReturn(fakeJwt);

        RecoveryJwtTokenDTO tokenDTO = userService.authenticatorUser(login);

        assertNotNull(tokenDTO);
        assertEquals(fakeJwt, tokenDTO.token());
    }
    @Test
    void shouldSaveUserWithEncodedPasswordAndAssignedRoles() {
        CreateUser createUser = mock(CreateUser.class);
        User user = new User();
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleName.ADMIN);
        List<Role> roles = List.of(role);

        when(createUser.role()).thenReturn(List.of(RoleName.ADMIN));
        when(userMapper.toEntity(createUser)).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(roles.get(0)));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.saveUser(createUser);

        verify(mailEventPublisher).sendWelcomeMail(any(), eq(user));
        verify(mailEventPublisher).sendPasswordChange(any(), eq(user));
        verify(userRepository).save(user);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(roles, result.getRoles());
    }

    @Test
    void shouldUpdatePasswordWhenAllValidationsPass() {
        String oldPassword = "old123";
        String newPassword = "new123456";

        PasswordAlterDTO dto = new PasswordAlterDTO(oldPassword, newPassword, newPassword);
        User user = new User();
        user.setPassword("encodedOld");

        when(auth.getName()).thenReturn("user@example.com");
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(SecurityUtils.getCurrentEmail()).thenReturn("user@example.com");
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(dto.newPassword())).thenReturn("encodedNew");

        user.setPassword("encodedOld");
        userService.passwordAlter(dto);

        verify(userRepository).save(user);
        assertEquals("encodedNew", user.getPassword());
    }

    @Test
    void shouldThrowWhenCurrentPasswordDoesNotMatch() {
        PasswordAlterDTO dto = new PasswordAlterDTO("wrong", "new", "new");
        User user = new User();
        user.setPassword("encoded");

        when(auth.getName()).thenReturn("user@example.com");
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(SecurityUtils.getCurrentEmail()).thenReturn("user@example.com");
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(false);

        assertThrows(PasswordNotMatchException.class, () -> userService.passwordAlter(dto));
    }

    @Test
    void shouldThrowWhenNewPasswordsDoNotMatch() {
        PasswordAlterDTO dto = new PasswordAlterDTO("correct", "new1", "new2");
        User user = new User();
        user.setPassword("encoded");

        when(auth.getName()).thenReturn("user@example.com");
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(SecurityUtils.getCurrentEmail()).thenReturn("user@example.com");
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);

        assertThrows(PasswordNotMatchException.class, () -> userService.passwordAlter(dto));
    }
}