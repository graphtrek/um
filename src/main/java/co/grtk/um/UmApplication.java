package co.grtk.um;

import co.grtk.um.config.RsaKeyProperties;
import co.grtk.um.model.User;
import co.grtk.um.model.UserStatus;
import co.grtk.um.repository.UserRepository;
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
	CommandLineRunner commandLineRunner(UserRepository users, PasswordEncoder encoder) {
		return args -> {
			users.save(new User("user", "user@user.com", encoder.encode("password"),"ROLE_USER", UserStatus.REGISTERED));
			users.save(new User("admin","admin@admin.com",encoder.encode("password"),"ROLE_USER,ROLE_ADMIN", UserStatus.REGISTERED));
		};
	}

}
