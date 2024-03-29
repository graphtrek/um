package co.grtk.um.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@FieldDefaults(level = PRIVATE)

@Component
@ConfigurationProperties(prefix = "application")
public class AppConfig {

    String applicationName;

    String adminUserName;
    String adminUserPassword;
    String adminUserEmail;

    String testUserName;
    String testUserPassword;
    String testUserEmail;

    String activityLogBaseUrl;

    String kafkaBootstrapServer;
    String kafkaTopicName;
    int kafkaTopicNumPartitions = 1;
    short kafkaTopicReplicationFactor = 1;

}
