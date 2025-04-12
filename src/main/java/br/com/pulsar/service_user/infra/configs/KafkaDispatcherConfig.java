package br.com.pulsar.service_user.infra.configs;

import br.com.pulsar.service_user.domain.dtos.kafka.PasswordChangeKafkaEvent;
import br.com.pulsar.service_user.domain.dtos.kafka.UserKafkaEvent;
import com.pulsar.common.common_kafka.dispatcher.KafkaEventDispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaDispatcherConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean(name = "welcomeKafkaTemplate")
    public KafkaTemplate<String, UserKafkaEvent> kafkaTemplateSendWelcome() {
        return KafkaEventDispatcher.createTemplate(bootstrapServers);
    }

    @Bean(name = "chanfePasswordKafkaTemplate")
    public KafkaTemplate<String, PasswordChangeKafkaEvent> kafkaTemplatePasswordChange() {
        return KafkaEventDispatcher.createTemplate(bootstrapServers);
    }
}
