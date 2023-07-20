package co.grtk.um.service;

import co.grtk.um.model.JwtToken;
import co.grtk.um.model.UmUser;
import co.grtk.um.repository.JwtTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
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


    @Transactional
    public JwtToken generateToken(UmUser umUser, String scope, String ipAddress) {
        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.MINUTES);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiration)
                .subject(umUser.getEmail())
                .claim("scope", scope)
                .build();

        String token = this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        JwtToken jwtToken = new JwtToken(umUser,scope,expiration,token, ipAddress);
        jwtTokenRepository.save(jwtToken);

        log.info("generated JwtToken email:{} scope:{} ipAddress:{}", umUser.getEmail(), scope, ipAddress);
        return jwtToken;
    }

}
