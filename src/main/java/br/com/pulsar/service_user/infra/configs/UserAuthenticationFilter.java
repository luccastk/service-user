package br.com.pulsar.service_user.infra.configs;

import br.com.pulsar.service_user.domain.models.User;
import br.com.pulsar.service_user.domain.repositories.UserRepository;
import br.com.pulsar.service_user.domain.services.user.UserDetailsImpl;
import br.com.pulsar.service_user.exception.MissingTokenException;
import br.com.pulsar.service_user.infra.jwt.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (checkIfEndpointNeedAuth(request)) {
            String token = recoveryToken(request);
            if (token != null) {
                String subject = jwtTokenService.getSubjectFromToken(token);
                User user = userRepository.findByEmailIgnoreCase(subject)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found."));
                UserDetailsImpl userDetails = new UserDetailsImpl(user);

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new MissingTokenException("Missing token.");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer", "").trim();
        }
        return null;
    }

    private boolean checkIfEndpointNeedAuth(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return !Arrays.asList(SecurityConfigs.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestUri);
    }
}
