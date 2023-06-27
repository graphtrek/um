package co.grtk.um.repository;

import co.grtk.um.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
   VerificationToken findByToken(String token);
}
