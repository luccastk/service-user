package br.com.pulsar.service_user.infra.jwt;

import br.com.pulsar.service_user.domain.repositories.UserRepository;
import br.com.pulsar.service_user.exception.MissingTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationFilterTest {

    @InjectMocks
    private UserAuthenticationFilter filter;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void cleanUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateWhenTokenIsPresent() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/v1/admin");
        request.addHeader("Authorization", "Bearer valid.token");

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtTokenService.getSubjectFromToken("valid.token")).thenReturn("user@example.com");
        when(jwtTokenService.getRolesFromToken("valid.token")).thenReturn(List.of("ROLE_ADMIN"));

        filter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("user@example.com", SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldSkipAuthenticationForPublicEndpoint() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/login");  // endpoint público
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldThrowExceptionWhenTokenMissing() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/password/change"); // endpoint protegido
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThrows(MissingTokenException.class, () -> {
            filter.doFilterInternal(request, response, filterChain);
        });
    }
}