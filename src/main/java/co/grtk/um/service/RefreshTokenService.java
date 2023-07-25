package co.grtk.um.service;

import co.grtk.um.exception.TokenRefreshException;
import co.grtk.um.model.JwtToken;
import co.grtk.um.model.RefreshToken;
import co.grtk.um.model.UmUser;
import co.grtk.um.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private static final int TIME_PERIOD_MINUTES = 60 * 24;
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken findOrCreateRefreshToken(UmUser umUser,String ipAddress) {
    RefreshToken refreshToken =
            refreshTokenRepository.
                    findByUmUser(umUser).map(this::verifyExpiration).
                    orElseGet(() -> new RefreshToken(umUser,TIME_PERIOD_MINUTES, ipAddress));
    refreshToken = refreshTokenRepository.save(refreshToken);
    log.info("generated RefreshToken email:{} scope:{}, ipAddress:{}", umUser.getEmail(), umUser.getRoles(), ipAddress);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken refreshToken) {
    Instant now = Instant.now();
    if (now.isAfter(refreshToken.getTokenExpirationTime())){
      log.warn("RefreshToken expired user:{} expiresAtUtcTime:{}", refreshToken.getUserEmail(), refreshToken.getExpiresAtUtcTime());
      throw new TokenRefreshException(refreshToken.getToken(), " refreshToken expired");
    }
    return refreshToken;
  }

  public void deleteByUser(UmUser umUser) {
      refreshTokenRepository.deleteByUmUser(umUser);
  }
  public List<RefreshToken> loadAllRefreshToken() {
    return refreshTokenRepository.findAll(Sort.by(Sort.Direction.DESC, "expiresAtUtcTime"));
  }
}
