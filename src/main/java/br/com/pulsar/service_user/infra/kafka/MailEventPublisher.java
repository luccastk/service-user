package br.com.pulsar.service_user.infra.kafka;

import br.com.pulsar.service_user.domain.dtos.kafka.PasswordChangeKafkaEvent;
import br.com.pulsar.service_user.domain.dtos.kafka.UserKafkaEvent;
import br.com.pulsar.service_user.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailEventPublisher {

    private final KafkaTemplate<String, UserKafkaEvent> welcomeTemplate;
    private final KafkaTemplate<String, PasswordChangeKafkaEvent> passwordChangeTemplate;

    public void sendWelcomeMail(UserKafkaEvent event, User user) {
        welcomeTemplate.send("mail.welcome", user.getId().toString(), event);
    }

    public void sendPasswordChange(PasswordChangeKafkaEvent event, User user) {
        passwordChangeTemplate.send("mail.password-change", user.getId().toString(), event);
    }
}
