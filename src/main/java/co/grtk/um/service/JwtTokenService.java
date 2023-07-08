package co.grtk.um.service;

import co.grtk.um.model.JwtToken;
import co.grtk.um.model.Principal;
import co.grtk.um.repository.JwtTokenRepository;
import co.grtk.um.repository.PrincipalRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class JwtTokenService {
    private final JwtTokenRepository jwtTokenRepository;
    private final PrincipalRepository principalRepository;
    private final JwtEncoder encoder;
    private final JwsHeader jwsHeader = JwsHeader.with(() -> "RS256").type("JWT").build();


    @Transactional
    public String generateToken(Authentication authentication) {
        Principal principal = principalRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found email: " + authentication.getName()));

        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        Instant expiration = now.plus(1, ChronoUnit.HOURS);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiration)
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        String token = this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        JwtToken jwtToken = new JwtToken(principal,scope,expiration,token);
        jwtTokenRepository.save(jwtToken);

        log.info("generated JwtToken email:{} scope:{}", authentication.getName(), scope);
        return token;
    }

}
