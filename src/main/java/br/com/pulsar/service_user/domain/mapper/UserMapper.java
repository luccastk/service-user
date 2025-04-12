package br.com.pulsar.service_user.domain.mapper;

import br.com.pulsar.service_user.domain.dtos.kafka.PasswordChangeKafkaEvent;
import br.com.pulsar.service_user.domain.dtos.kafka.UserKafkaEvent;
import br.com.pulsar.service_user.domain.dtos.user.CreateUser;
import br.com.pulsar.service_user.domain.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUser dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setName(dto.name());

        return user;
    }

    public UserKafkaEvent toKafkaUser(CreateUser entity) {
        if (entity == null) {
            return null;
        }

        return new UserKafkaEvent(
                entity.name(),
                entity.email()
        );
    }

    public PasswordChangeKafkaEvent toKafkaUserAndPassword(CreateUser entity, String password) {
        if (entity == null) {
            return null;
        }

        if (password == null) {
            return null;
        }

        return new PasswordChangeKafkaEvent(
                toKafkaUser(entity),
                password
        );
    }
}
