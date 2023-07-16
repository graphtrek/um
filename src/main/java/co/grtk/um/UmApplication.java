package co.grtk.um;

import co.grtk.um.config.ApplicationConfig;
import co.grtk.um.config.RsaKeyProperties;
import co.grtk.um.exception.UmException;
import co.grtk.um.model.UmUser;
import co.grtk.um.model.UmUserStatus;
import co.grtk.um.repository.UmUserRepository;
import co.grtk.um.service.TotpService;
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
	CommandLineRunner commandLineRunner(UmUserRepository users, PasswordEncoder encoder, TotpService totpService, ApplicationConfig applicationConfig) {
		String secret = totpService.generateSecret();
		log.info("Secret:{}",secret);
		try {
			String actualCode = totpService.generateCode(secret, 30);
			boolean isValidCode = totpService.isValidCode(secret, actualCode,30);
			log.info("ActualCode {} valid:{}", actualCode, isValidCode);
		} catch (Exception e) {
			throw new UmException("Unable to start application", e);
		}
		return args ->
			users.save(new UmUser(
							applicationConfig.getAdminUserName(),
							applicationConfig.getAdminUserEmail(),
							encoder.encode(applicationConfig.getAdminUserPassword()),
							applicationConfig.getAdminUserRoles(),
							UmUserStatus.REGISTERED,
							secret)
			);
		}


}
