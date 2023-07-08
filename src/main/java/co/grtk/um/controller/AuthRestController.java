package co.grtk.um.controller;

import co.grtk.um.service.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthRestController.class);
    private final JwtTokenService jwtTokenService;

    public AuthRestController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        LOG.info("Token requested for user:{}", authentication.getName());
        String token = jwtTokenService.generateToken(authentication);
        LOG.info("Token granted for user:{} token:{}",authentication.getName(), token);
        return token;
    }
}