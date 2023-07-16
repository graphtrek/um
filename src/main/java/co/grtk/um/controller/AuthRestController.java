package co.grtk.um.controller;

import co.grtk.um.dto.TokenRequest;
import co.grtk.um.dto.TokenResponse;
import co.grtk.um.exception.InvalidVerificationTokenException;
import co.grtk.um.model.UmUser;
import co.grtk.um.service.JwtTokenService;
import co.grtk.um.service.TotpService;
import co.grtk.um.service.UmUserDetailsService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthRestController.class);
    private final JwtTokenService jwtTokenService;
    private final TotpService totpService;
    private final UmUserDetailsService userDetailsService;


    public AuthRestController(JwtTokenService jwtTokenService,
                              TotpService totpService,
                              UmUserDetailsService umUserDetailsService) {
        this.jwtTokenService = jwtTokenService;
        this.totpService = totpService;
        this.userDetailsService = umUserDetailsService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> token(@RequestBody TokenRequest tokenRequest, Authentication authentication, HttpServletRequest request) {
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase().contains("ADMIN"));
        LOG.info("Token requested for user:{} isAdmin:{}", authentication.getName(),isAdmin);
        TokenResponse tokenResponse = new TokenResponse();
        if(isAdmin) {
            UmUser umUser = userDetailsService.loadUserByEmail(authentication.getName());
            if(StringUtils.isBlank(tokenRequest.getCode())) {
                tokenResponse.setQrCode(totpService.getUriForImage(umUser.getSecret()));
                LOG.info("Admin {} 2FA authentication QRCode: {}", authentication.getName(), tokenResponse.getQrCode());
            } else if(totpService.verifyCode(tokenRequest.getCode(),umUser.getSecret())) {
                LOG.info("Admin {} 2FA has valid code: {}", authentication.getName(), tokenRequest.getCode());
                String token = jwtTokenService.generateToken(authentication, request);
                tokenResponse.setAccessToken(token);
                LOG.info("Token granted for user:{} token:{}", authentication.getName(), token);
            } else {
                LOG.error("InvalidVerificationTokenException user:{}, code:{}", umUser.getEmail(), tokenRequest.getCode());
                throw new InvalidVerificationTokenException("User " + umUser.getName() + " code:" + tokenRequest.getCode());
            }
        } else {
            String token = jwtTokenService.generateToken(authentication, request);
            tokenResponse.setAccessToken(token);
            LOG.info("Token granted for user:{} token:{}", authentication.getName(), token);
        }

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }
}