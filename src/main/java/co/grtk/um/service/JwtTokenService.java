package co.grtk.um.service;

import co.grtk.um.model.JwtToken;
import co.grtk.um.model.UmUser;
import co.grtk.um.repository.JwtTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class JwtTokenService {
    private final JwtTokenRepository jwtTokenRepository;
    private final JwtEncoder encoder;
    private final JwsHeader jwsHeader = JwsHeader.with(() -> "RS256").type("JWT").build();

    public Jwt generateToken(String email, String scope, int timePeriodMinutes) {
        Instant now = Instant.now();
        Instant expiration = now.plus(timePeriodMinutes, ChronoUnit.MINUTES);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiration)
                .subject(email)
                .claim("scope", scope)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
    }

    @Transactional
    public JwtToken saveJwtToken(UmUser umUser, Jwt jwt, int expiresIn, String ipAddress) {
        JwtToken jwtToken = new JwtToken(umUser,jwt, expiresIn, ipAddress);
        log.info("generated JwtToken email:{} scope:{} ipAddress:{}", umUser.getEmail(), jwtToken.getScope(), ipAddress);
        return  jwtTokenRepository.save(jwtToken);
    }

    public List<JwtToken> loadAllJwtToken() {
        return jwtTokenRepository.findByExpiresAtUtcTimeAfterOrderByExpiresAtUtcTimeDesc(Instant.now());
    }

}
