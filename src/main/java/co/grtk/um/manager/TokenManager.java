package co.grtk.um.manager;

import co.grtk.um.dto.TokenResponse;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class TokenManager {

    private final JwtTokenService jwtTokenService;
    private final TotpService totpService;
    private final UmUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public TokenResponse getJwtToken(Authentication authentication, String ipAddress, String totpCode) {
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase().contains("ADMIN"));
        log.info("Token requested for user:{} isAdmin:{}", authentication.getName(),isAdmin);
        TokenResponse tokenResponse;
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        UmUser umUser = userDetailsService.loadUserByEmail(authentication.getName());

        if(MfaType.APP == umUser.getMfaType()) {
            tokenResponse = createTokenResponseForTotpCode(umUser,totpCode,scope,ipAddress);
        } else {
            tokenResponse = createTokenResponseForUser(umUser,scope,ipAddress);
        }
        return tokenResponse;
    }

    private TokenResponse createTokenResponseForTotpCode(UmUser umUser, String totpCode, String scope, String ipAddress) {
        TokenResponse tokenResponse;
        if(StringUtils.isBlank(totpCode)) {
            tokenResponse = createTokenResponseForQrCode(umUser, scope);
            log.info("Admin {} 2FA authentication QRCode: {}", umUser.getEmail(), tokenResponse.getQrCode());
        } else {
            totpService.verifyCode(totpCode, umUser.getSecret());
            log.info("Admin {} 2FA has valid code: {}", umUser.getEmail(), totpCode);
            tokenResponse = createTokenResponseForUser(umUser,scope,ipAddress);
            log.info("Token granted for user:{} token:{}", umUser.getEmail(), tokenResponse.getAccessToken());
        }
        return tokenResponse;
    }

    private TokenResponse createTokenResponseForQrCode(UmUser umUser, String scope) {
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setEmail(umUser.getEmail());
        tokenResponse.setUserName(umUser.getName());
        tokenResponse.setScope(scope);
        tokenResponse.setQrCode(totpService.getUriForImage(umUser.getSecret()));
        return tokenResponse;
    }

    private TokenResponse createTokenResponseForUser(UmUser umUser,String scope, String ipAddress) {
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setEmail(umUser.getEmail());
        tokenResponse.setUserName(umUser.getName());
        tokenResponse.setScope(scope);
        JwtToken jwtToken = jwtTokenService.generateToken(umUser, scope, ipAddress);
        RefreshToken refreshToken = refreshTokenService.findOrCreateRefreshToken(umUser);
        tokenResponse.setAccessToken(jwtToken.getToken());
        tokenResponse.setRefreshToken(refreshToken.getToken());
        return tokenResponse;
    }
}
