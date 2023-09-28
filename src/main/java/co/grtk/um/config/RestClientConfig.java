package co.grtk.um.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient getActivityLogClient() {
        return RestClient.builder().baseUrl("http://localhost:8082/api").build();
    }
}
