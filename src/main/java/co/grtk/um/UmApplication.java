package co.grtk.um;

import co.grtk.um.config.RsaKeyProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;


@Slf4j
@EnableAsync
@OpenAPIDefinition
@EnableConfigurationProperties(RsaKeyProperties.class)
@SecurityScheme(name = "um", scheme = "Bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@SpringBootApplication
public class UmApplication {
	public static void main(String[] args) {
		SpringApplication.run(UmApplication.class, args);
	}

}
