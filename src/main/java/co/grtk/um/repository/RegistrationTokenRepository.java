package co.grtk.um.repository;

import co.grtk.um.model.RegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, Long> {
   Optional<RegistrationToken> findByToken(String token);
}
