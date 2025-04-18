package br.com.pulsar.service_user.infra.kafka;

import br.com.pulsar.service_user.domain.dtos.kafka.PasswordChangeKafkaEvent;
import br.com.pulsar.service_user.domain.dtos.kafka.UserKafkaEvent;
import br.com.pulsar.service_user.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailEventPublisherTest {

    @Mock
    private KafkaTemplate<String, UserKafkaEvent> welcomeTemplate;

    @Mock
    private KafkaTemplate<String, PasswordChangeKafkaEvent> passwordChangeTemplate;

    private MailEventPublisher mailEventPublisher;

    private ArgumentCaptor<UserKafkaEvent> welcomeCaptor;
    private ArgumentCaptor<PasswordChangeKafkaEvent> passwordCaptor;

    private User user;
    private UserKafkaEvent event;

    @BeforeEach
    void setUp() {
        mailEventPublisher = new MailEventPublisher(welcomeTemplate, passwordChangeTemplate);

        // captors
        welcomeCaptor = ArgumentCaptor.forClass(UserKafkaEvent.class);
        passwordCaptor = ArgumentCaptor.forClass(PasswordChangeKafkaEvent.class);

        // dados de teste
        user = new User();
        user.setId(UUID.randomUUID());
        event = new UserKafkaEvent("name", "email@example.com");
    }

    @Test
    void shouldSendWelcomeMail() {
        // act
        mailEventPublisher.sendWelcomeMail(event, user);

        // assert:
        verify(welcomeTemplate).send(
                eq("mail.welcome"),
                eq(user.getId().toString()),
                welcomeCaptor.capture()
        );

        UserKafkaEvent captured = welcomeCaptor.getValue();
        assertEquals("name", captured.name());
        assertEquals("email@example.com", captured.email());
    }

    @Test
    void shouldSendPasswordChangeMail() {
        // arrange
        PasswordChangeKafkaEvent passwordEvent =
                new PasswordChangeKafkaEvent(event, "newPassword");

        // act
        mailEventPublisher.sendPasswordChange(passwordEvent, user);

        // assert
        verify(passwordChangeTemplate).send(
                eq("mail.password-change"),
                eq(user.getId().toString()),
                passwordCaptor.capture()
        );

        PasswordChangeKafkaEvent captured = passwordCaptor.getValue();
        assertEquals("newPassword", captured.password());
        assertEquals("name", captured.user().name());
        assertEquals("email@example.com", captured.user().email());
    }
}