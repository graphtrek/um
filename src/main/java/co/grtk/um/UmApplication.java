package co.grtk.um;

import co.grtk.um.config.RsaKeyProperties;
import co.grtk.um.model.Principal;
import co.grtk.um.model.PrincipalStatus;
import co.grtk.um.repository.PrincipalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@Slf4j
@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class UmApplication {
	public static void main(String[] args) {
		SpringApplication.run(UmApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(PrincipalRepository users, PasswordEncoder encoder) {
		return args -> {
			users.save(new Principal("user", "user@graphtrek.co", encoder.encode("G1rafhus"),"ROLE_USER", PrincipalStatus.REGISTERED));
			users.save(new Principal("admin","admin@graphtrek.co",encoder.encode("G1rafhus"),"ROLE_USER,ROLE_ADMIN", PrincipalStatus.REGISTERED));
		};
	}

}
