package co.grtk.um.repository;

import co.grtk.um.model.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
   JwtToken findByToken(String token);
   List<JwtToken> findByExpiresAtUtcTimeAfterOrderByExpiresAtUtcTimeDesc(Instant instant);
}
