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
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@AllArgsConstructor
public class TokenManager {

    private final JwtTokenService jwtTokenService;
    private final TotpService totpService;
    private final UmUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public TokenResponse getJwtToken(Authentication authentication, String ipAddress, String totpCode) {
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase().contains("ADMIN"));
        log.info("Token requested for user:{} isAdmin:{}", authentication.getName(),isAdmin);
        TokenResponse tokenResponse;
        UmUser umUser = userDetailsService.loadUserByEmail(authentication.getName());
        deleteRefreshToken(umUser.getEmail());
        if(MfaType.APP == umUser.getMfaType()) {
            tokenResponse = createTokenResponseForTotpCode(umUser,totpCode,ipAddress);
        } else {
            tokenResponse = createTokenResponseForUser(umUser,ipAddress);
        }
        return tokenResponse;
    }

    private TokenResponse createTokenResponseForTotpCode(UmUser umUser, String totpCode, String ipAddress) {
        TokenResponse tokenResponse;
        if(StringUtils.isBlank(totpCode)) {
            tokenResponse = createTokenResponseForQrCode(umUser);
            log.info("Admin {} 2FA authentication QRCode: {}", umUser.getEmail(), tokenResponse.getQrCode());
        } else {
            totpService.verifyCode(totpCode, umUser.getSecret());
            log.info("Admin {} 2FA has valid code: {}", umUser.getEmail(), totpCode);
            tokenResponse = createTokenResponseForUser(umUser, ipAddress);
            log.info("Token granted for user:{} token:{}", umUser.getEmail(), tokenResponse.getAccessToken());
        }
        return tokenResponse;
    }

    private TokenResponse createTokenResponseForQrCode(UmUser umUser) {
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setEmail(umUser.getEmail());
        tokenResponse.setUserName(umUser.getName());
        tokenResponse.setScope(umUser.getRoles());
        tokenResponse.setQrCode(totpService.getUriForImage(umUser.getSecret()));
        return tokenResponse;
    }

    private TokenResponse createTokenResponseForUser(UmUser umUser, String ipAddress) {
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setEmail(umUser.getEmail());
        tokenResponse.setUserName(umUser.getName());
        tokenResponse.setScope(umUser.getRoles());

        JwtToken jwtToken = jwtTokenService.generateToken(umUser, ipAddress);
        tokenResponse.setAccessToken(jwtToken.getToken());
        tokenResponse.setExpiresIn(jwtToken.getTimePeriodMinutes());

        RefreshToken refreshToken = refreshTokenService.findOrCreateRefreshToken(umUser, ipAddress);
        tokenResponse.setRefreshToken(refreshToken.getToken());
        tokenResponse.setRefreshExpiresIn(refreshToken.getTimePeriodMinutes());
        return tokenResponse;
    }

    @Transactional
    public void deleteRefreshToken(String email) {
        UmUser umUser = userDetailsService.loadUserByEmail(email);
        refreshTokenService.deleteByUser(umUser);
    }

    public TokenResponse refreshToken(String refreshToken, String ipAddress) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUmUser)
                .map(umUser -> createTokenResponseForUser(umUser,ipAddress))
                .orElseThrow(() -> new TokenRefreshException(refreshToken,
                        "Refresh token is not in database!"));
    }
}
