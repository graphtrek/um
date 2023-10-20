package co.grtk.um.controller;

import co.grtk.um.dto.TokenRequest;
import co.grtk.um.dto.TokenResponse;
import co.grtk.um.manager.TokenManager;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthRestController.class);
    private final TokenManager tokenManager;

    public AuthRestController(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @PostMapping("/api/token")
    public ResponseEntity<TokenResponse> token(
            @RequestBody TokenRequest tokenRequest,
            Authentication authentication,
            HttpServletRequest request) {

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        String totpCode = tokenRequest.getCode();
        LOG.info("/token called user:{} ipAddress:{} totpCode:{}",
                authentication.getName(), ipAddress, totpCode);
        TokenResponse tokenResponse =
                tokenManager.getJwtToken(authentication, ipAddress, tokenRequest.getCode());
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @PostMapping("/api/refreshToken")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody TokenRequest tokenRequest,
            HttpServletRequest request) {

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        String refreshToken = tokenRequest.getRefreshToken();
        LOG.info("/api/refreshToken called ipAddress:{} refreshToken:{}", ipAddress, refreshToken);
        TokenResponse tokenResponse = tokenManager.refreshToken(refreshToken,ipAddress);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }


    @SecurityRequirement(name = "um")
    @PostMapping("/api/logout")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<String> logoutUser(Authentication authentication) {
        LOG.info("/api/logout called user:{}", authentication.getName());
        tokenManager.deleteRefreshToken(authentication.getName());
        return new ResponseEntity<>("Log out successful!", HttpStatus.OK);
    }
}