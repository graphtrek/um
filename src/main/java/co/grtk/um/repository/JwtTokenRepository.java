package co.grtk.um.repository;

import co.grtk.um.model.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
   JwtToken findByToken(String token);
}
