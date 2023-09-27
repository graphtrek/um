package co.grtk.um.manager;

import co.grtk.um.dto.TokenResponse;
import co.grtk.um.exception.TokenRefreshException;
import co.grtk.um.model.*;
import co.grtk.um.repository.UmUserRepository;
import co.grtk.um.service.JwtTokenService;
import co.grtk.um.service.RefreshTokenService;
import co.grtk.um.service.TotpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UmUserRepository umUserRepository;
    private final RefreshTokenService refreshTokenService;

    public static final int JWT_TIME_PERIOD_MINUTES = 5;
    private static final String ERRORLOG = "User not found email: ";

    @Transactional
    public TokenResponse getJwtToken(Authentication authentication, String ipAddress, String totpCode) {
        boolean isAdmin = isAdmin(authentication);
        log.info("Token requested for user:{} isAdmin:{}", authentication.getName(),isAdmin);
        UmUser umUser = findUmUser(authentication.getName());
        deleteRefreshToken(umUser.getEmail());

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setEmail(umUser.getEmail());
        tokenResponse.setUserName(umUser.getName());

        // if APP user does not have a totpCode then send QRCode
        if(MfaType.APP == umUser.getMfaType() && StringUtils.isBlank(totpCode)) {
            tokenResponse.setQrCode(totpService.getUriForImage(umUser.getSecret(), umUser.getName()));
            log.info("USer {} 2FA authentication QRCode: {}", umUser.getEmail(), tokenResponse.getQrCode());

        } else {
            // passed totpcode needs to be validated
            if (StringUtils.isNotBlank(totpCode)) {
                totpService.verifyCode(totpCode, umUser.getSecret());
                log.info("User {} 2FA has valid code: {}", umUser.getEmail(), totpCode);
            }

            // all ok generate JWTToken
            JwtToken jwtToken = generateJwtToken(umUser, ipAddress);
            tokenResponse.setScope(jwtToken.getScope());

            tokenResponse.setAccessToken(jwtToken.getToken());
            tokenResponse.setExpiresIn(jwtToken.getTimePeriodMinutes());

            RefreshToken refreshToken = refreshTokenService.findOrCreateRefreshToken(umUser, ipAddress);
            tokenResponse.setRefreshToken(refreshToken.getToken());
            tokenResponse.setRefreshExpiresIn(refreshToken.getTimePeriodMinutes());

            log.info("Token granted for user:{} token:{}", umUser.getEmail(), tokenResponse.getAccessToken());
        }
        return tokenResponse;
    }

    private JwtToken generateJwtToken(UmUser umUser, String ipAddress) {
        String scope = umUser.getRoles().stream()
                .map(role -> role.name)
                .collect(Collectors.joining(","));
        Jwt jwt = jwtTokenService.generateToken(umUser.getEmail(), scope, JWT_TIME_PERIOD_MINUTES);
        return jwtTokenService.saveJwtToken(umUser,jwt, JWT_TIME_PERIOD_MINUTES, ipAddress);
    }

    @Transactional
    public void deleteRefreshToken(String email) {
        UmUser umUser = findUmUser(email);
        refreshTokenService.deleteByUser(umUser);
    }

    private UmUser findUmUser(String email) {
        return umUserRepository
                .findByEmailAndStatus(email, UmUserStatus.REGISTERED)
                .orElseThrow(() -> new UsernameNotFoundException(ERRORLOG + email));
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

        JwtToken jwtToken = generateJwtToken(umUser, ipAddress);
        tokenResponse.setAccessToken(jwtToken.getToken());
        tokenResponse.setExpiresIn(jwtToken.getTimePeriodMinutes());

        tokenResponse.setScope(jwtToken.getScope());

        RefreshToken refreshToken = refreshTokenService.findOrCreateRefreshToken(umUser, ipAddress);
        tokenResponse.setRefreshToken(refreshToken.getToken());
        tokenResponse.setRefreshExpiresIn(refreshToken.getTimePeriodMinutes());



        return tokenResponse;
    }

    public static boolean isAdmin(Authentication authentication) {
        if(authentication == null)
            return false;
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().contains("SCOPE_ROLE_ADMIN"));
    }

}
