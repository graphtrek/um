package co.grtk.um.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Configuration
public class RestClientConfig {

    private final AppConfig appConfig;
    @Bean
    public RestClient getActivityLogClient() {
        return RestClient
                .builder()
                .baseUrl(appConfig.getActivityLogBaseUrl())
                .build();
    }
}
