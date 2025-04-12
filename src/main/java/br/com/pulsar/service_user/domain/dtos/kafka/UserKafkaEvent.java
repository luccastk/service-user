package br.com.pulsar.service_user.domain.dtos.kafka;

public record UserKafkaEvent(
        String name,
        String email
) {
}
