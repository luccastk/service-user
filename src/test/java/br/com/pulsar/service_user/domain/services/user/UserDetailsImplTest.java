package br.com.pulsar.service_user.domain.services.user;

import br.com.pulsar.service_user.domain.models.Role;
import br.com.pulsar.service_user.domain.models.User;
import br.com.pulsar.service_user.domain.models.enums.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleName.ADMIN);

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");
        user.setRoles(List.of(role));

        userDetails = new UserDetailsImpl(user);
    }

    @Test
    void shouldReturnCorrectUsernameAndPassword() {
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("encoded-password", userDetails.getPassword());
    }

    @Test
    void shouldReturnCorrectAuthorities() {
        var authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void shouldReturnAccountStatusAsTrue() {
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

}