package br.com.pulsar.service_user.infra.jwt;

import br.com.pulsar.service_user.domain.models.Role;
import br.com.pulsar.service_user.domain.models.User;
import br.com.pulsar.service_user.domain.models.enums.RoleName;
import br.com.pulsar.service_user.domain.services.user.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    private final String SECRET = "test-secret";
    private final String ISSUER = "test-issuer";

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenService();
        ReflectionTestUtils.setField(jwtTokenService, "secret_key", SECRET);
        ReflectionTestUtils.setField(jwtTokenService, "issuer", ISSUER);

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("pass");

        Role role = new Role();
        role.setName(RoleName.ADMIN);
        user.setRoles(List.of(role));

        userDetails = new UserDetailsImpl(user);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String token = jwtTokenService.generateToken(userDetails);
        assertNotNull(token);

        String subject = jwtTokenService.getSubjectFromToken(token);
        assertEquals("user@example.com", subject);
    }

    @Test
    void shouldExtractRolesFromToken() {
        String token = jwtTokenService.generateToken(userDetails);
        List<String> roles = jwtTokenService.getRolesFromToken(token);
        assertTrue(roles.contains("ROLE_ADMIN"));
    }

    @Test
    void shouldThrowWhenInvalidToken() {
        String invalidToken = "invalid.token.value";
        assertThrows(Exception.class, () -> jwtTokenService.getSubjectFromToken(invalidToken));
    }
}