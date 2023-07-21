package co.grtk.um.service;

import co.grtk.um.exception.TokenRefreshException;
import co.grtk.um.model.RefreshToken;
import co.grtk.um.model.UmUser;
import co.grtk.um.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private static final int TIME_PERIOD_MINUTES = 60 * 24;
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Transactional
  public RefreshToken findOrCreateRefreshToken(UmUser umUser) {
    RefreshToken refreshToken =
            refreshTokenRepository.
                    findByUmUser(umUser).
                    orElseGet(() -> new RefreshToken(umUser,TIME_PERIOD_MINUTES));
    verifyExpiration(refreshToken);
    return refreshTokenRepository.save(refreshToken);
  }

  public void verifyExpiration(RefreshToken refreshToken) {
    Instant now = Instant.now();
    if (now.isAfter(refreshToken.getTokenExpirationTime())){
      throw new TokenRefreshException(refreshToken.getToken(), " refreshToken expired");
    }
  }

  @Transactional
  public int deleteByUser(UmUser umUser) {
    return refreshTokenRepository.deleteByUmUser(umUser);
  }
}
