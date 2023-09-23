package co.grtk.um.manager;

import co.grtk.um.dto.TokenResponse;
import co.grtk.um.exception.TokenRefreshException;
import co.grtk.um.model.JwtToken;
import co.grtk.um.model.MfaType;
import co.grtk.um.model.RefreshToken;
import co.grtk.um.model.UmUser;
import co.grtk.um.service.JwtTokenService;
import co.grtk.um.service.RefreshTokenService;
import co.grtk.um.service.TotpService;
import co.grtk.um.service.UmUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class TokenManager {


    private final JwtTokenService jwtTokenService;
    private final TotpService totpService;
    private final UmUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public static final int JWT_TIME_PERIOD_MINUTES = 5;

    @Transactional
    public TokenResponse getJwtToken(Authentication authentication, String ipAddress, String totpCode) {
        boolean isAdmin =
                authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase().contains("ADMIN"));
        log.info("Token requested for user:{} isAdmin:{}", authentication.getName(),isAdmin);
        UmUser umUser = userDetailsService.findByEmail(authentication.getName());
        deleteRefreshToken(umUser.getEmail());

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setEmail(umUser.getEmail());
        tokenResponse.setUserName(umUser.getName());
        tokenResponse.setScope(umUser.getRoles().stream()
                .map(role -> role.name).collect(Collectors.joining(",")));

        // if APP user does not have a totpCode then send QRCode
        if(MfaType.APP == umUser.getMfaType() && StringUtils.isBlank(totpCode)) {
            tokenResponse.setQrCode(totpService.getUriForImage(umUser.getSecret()));
            log.info("Admin {} 2FA authentication QRCode: {}", umUser.getEmail(), tokenResponse.getQrCode());

        } else {
            // passed totpcode needs to be validated
            if (StringUtils.isNotBlank(totpCode)) {
                totpService.verifyCode(totpCode, umUser.getSecret());
                log.info("Admin {} 2FA has valid code: {}", umUser.getEmail(), totpCode);
            }

            String scope = umUser.getRoles().stream()
                            .map(role -> role.name)
                            .collect(Collectors.joining(","));
            // all ok generate JWTToken
            Jwt jwt = jwtTokenService.generateToken(umUser.getEmail(), scope, JWT_TIME_PERIOD_MINUTES);

            JwtToken jwtToken = jwtTokenService.saveJwtToken(umUser,jwt, JWT_TIME_PERIOD_MINUTES, ipAddress);
            log.info("generated JwtToken email:{} scope:{} ipAddress:{}", umUser.getEmail(), scope, ipAddress);

            tokenResponse.setAccessToken(jwtToken.getToken());
            tokenResponse.setExpiresIn(jwtToken.getTimePeriodMinutes());
            log.info("Token granted for user:{} token:{}", umUser.getEmail(), tokenResponse.getAccessToken());
        }
        return tokenResponse;
    }


    @Transactional
    public void deleteRefreshToken(String email) {
        UmUser umUser = userDetailsService.findByEmail(email);
        refreshTokenService.deleteByUser(umUser);
    }

    @Transactional
    public TokenResponse refreshToken(String token, String ipAddress) {
        UmUser umUser = refreshTokenService.findByToken(token)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUmUser)
                .orElseThrow(() -> new TokenRefreshException(token,
                        "Refresh token is not in database!"));

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setEmail(umUser.getEmail());
        tokenResponse.setUserName(umUser.getName());
        tokenResponse.setScope(umUser.getRoles().stream()
                .map(role -> role.name)
                .collect(Collectors.joining(",")));

        RefreshToken refreshToken = refreshTokenService.findOrCreateRefreshToken(umUser, ipAddress);
        tokenResponse.setRefreshToken(refreshToken.getToken());
        tokenResponse.setRefreshExpiresIn(refreshToken.getTimePeriodMinutes());
        return tokenResponse;
    }
}
