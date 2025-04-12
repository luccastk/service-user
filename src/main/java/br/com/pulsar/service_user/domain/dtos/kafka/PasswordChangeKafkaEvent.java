package br.com.pulsar.service_user.domain.dtos.kafka;

public record PasswordChangeKafkaEvent(
        UserKafkaEvent user,
        String password
) {
}
