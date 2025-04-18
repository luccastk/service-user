package br.com.pulsar.service_user.domain.mapper;

import br.com.pulsar.service_user.domain.dtos.kafka.PasswordChangeKafkaEvent;
import br.com.pulsar.service_user.domain.dtos.kafka.UserKafkaEvent;
import br.com.pulsar.service_user.domain.dtos.user.CreateUser;
import br.com.pulsar.service_user.domain.models.User;
import br.com.pulsar.service_user.domain.models.enums.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    private CreateUser createUser;

    @BeforeEach
    void setUp() {
        createUser = new CreateUser(
                "John", "john@example.com", List.of(RoleName.ADMIN)
        );
    }

    @Test
    void shouldMapToEntityFromCreateUser() {
        User result = userMapper.toEntity(createUser);

        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        assertEquals("John", result.getName());
    }

    @Test
    void shouldReturnNullWhenToEntityReceivesNull() {
        assertNull(userMapper.toEntity(null));
    }

    @Test
    void shouldMapToKafkaUserFromCreateUser() {
        UserKafkaEvent result = userMapper.toKafkaUser(createUser);

        assertNotNull(result);
        assertEquals("John", result.name());
        assertEquals("john@example.com", result.email());
    }

    @Test
    void shouldReturnNullWhenToKafkaUserReceivesNull() {
        assertNull(userMapper.toKafkaUser(null));
    }

    @Test
    void shouldMapToPasswordChangeKafkaEvent() {
        PasswordChangeKafkaEvent result = userMapper.toKafkaUserAndPassword(createUser, "encoded123");

        assertNotNull(result);
        assertNotNull(result.user());
        assertEquals("John", result.user().name());
        assertEquals("encoded123", result.password());
    }

    @Test
    void shouldReturnNullWhenPasswordIsNull() {
        assertNull(userMapper.toKafkaUserAndPassword(createUser, null));
    }

    @Test
    void shouldReturnNullWhenUserIsNullInPasswordChangeKafkaEvent() {
        assertNull(userMapper.toKafkaUserAndPassword(null, "encoded123"));
    }
}