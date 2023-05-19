package co.grtk.um.controller;

import co.grtk.um.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthRestController.class);
    private final TokenService tokenService;

    public AuthRestController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        LOG.info("Token requested for user: '{}'", authentication.getName());
        String token = tokenService.generateToken(authentication);
        LOG.info("Token granted: {}", token);
        return token;
    }

}