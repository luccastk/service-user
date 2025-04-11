package br.com.pulsar.service_user.domain.dtos;

public record IndexResponseDTO(
        String message
) {
    public IndexResponseDTO() {
        this("User API is running");
    }
}
