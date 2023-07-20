package co.grtk.um.repository;

import co.grtk.um.model.RefreshToken;
import co.grtk.um.model.UmUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);
  Optional<RefreshToken> findByUmUser(UmUser umUser);

  @Modifying
  int deleteByUmUser(UmUser user);
}
