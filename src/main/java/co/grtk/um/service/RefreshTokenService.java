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
                    findByUmUser(umUser).map(this::verifyExpiration).
                    orElseGet(() -> new RefreshToken(umUser,TIME_PERIOD_MINUTES));
    return refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken verifyExpiration(RefreshToken refreshToken) {
    Instant now = Instant.now();
    if (now.isAfter(refreshToken.getTokenExpirationTime())){
      throw new TokenRefreshException(refreshToken.getToken(), " refreshToken expired");
    }
    return refreshToken;
  }

  @Transactional
  public void deleteByUser(UmUser umUser) {
      refreshTokenRepository.deleteByUmUser(umUser);
  }
}
