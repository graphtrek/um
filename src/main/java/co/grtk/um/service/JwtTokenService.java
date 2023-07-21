package co.grtk.um.service;

import co.grtk.um.model.JwtToken;
import co.grtk.um.model.UmUser;
import co.grtk.um.repository.JwtTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@AllArgsConstructor
@Service
public class JwtTokenService {
    private final JwtTokenRepository jwtTokenRepository;
    private final JwtEncoder encoder;
    private final JwsHeader jwsHeader = JwsHeader.with(() -> "RS256").type("JWT").build();
    private static final int TIME_PERIOD_MINUTES = 5;

    @Transactional
    public JwtToken generateToken(UmUser umUser, String ipAddress) {
        Instant now = Instant.now();
        Instant expiration = now.plus(TIME_PERIOD_MINUTES, ChronoUnit.MINUTES);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiration)
                .subject(umUser.getEmail())
                .claim("scope", umUser.getRoles())
                .build();

        Jwt jwt = this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims));

        JwtToken jwtToken = new JwtToken(umUser,jwt,TIME_PERIOD_MINUTES, ipAddress);
        jwtTokenRepository.save(jwtToken);

        log.info("generated JwtToken email:{} scope:{} ipAddress:{}", umUser.getEmail(), umUser.getRoles(), ipAddress);
        return jwtToken;
    }

}
