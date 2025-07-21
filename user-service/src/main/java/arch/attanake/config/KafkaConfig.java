package arch.attanake.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.topics.user-created}")
    private String userCreatedTopic;

    @Value("${spring.kafka.topics.user-roles-updated}")
    private String userRolesUpdatedTopic;

    @Bean
    public NewTopic userCreatedTopic() {
        return TopicBuilder.name(userCreatedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userRolesUpdatedTopic() {
        return TopicBuilder.name(userRolesUpdatedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
