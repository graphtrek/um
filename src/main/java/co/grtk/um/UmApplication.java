package co.grtk.um;

import co.grtk.um.config.RsaKeyProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;


@Slf4j
@EnableAsync
@OpenAPIDefinition
@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class UmApplication {
	public static void main(String[] args) {
		SpringApplication.run(UmApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		log.info("UmApplication starting ...");
		return args -> {};
	}


}
