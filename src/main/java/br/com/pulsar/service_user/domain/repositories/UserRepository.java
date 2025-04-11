package br.com.pulsar.service_user.domain.repositories;

import br.com.pulsar.service_user.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String username);

    boolean existsByNameIgnoreCase(String name);
}
