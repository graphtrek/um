package co.grtk.um.controller;

import co.grtk.um.dto.JwtTokenDTO;
import co.grtk.um.dto.PasswordResetTokenDTO;
import co.grtk.um.dto.RefreshTokenDTO;
import co.grtk.um.dto.RegistrationTokenDTO;
import co.grtk.um.service.JwtTokenService;
import co.grtk.um.service.PasswordResetTokenService;
import co.grtk.um.service.RefreshTokenService;
import co.grtk.um.service.RegistrationTokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@SecurityRequirement(name = "um")
public class TokensRestController {
    public final JwtTokenService jwtTokenService;
    public final PasswordResetTokenService passwordResetTokenService;
    public final RegistrationTokenService registrationTokenService;
    public final RefreshTokenService refreshTokenService;
    public final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/jwt-tokens")
    public ResponseEntity<List<JwtTokenDTO>> getJwtTokens() {
        return new ResponseEntity<>(
                jwtTokenService.loadAllJwtToken().stream().
                        map(token -> modelMapper.map(token, JwtTokenDTO.class)).toList(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/refresh-tokens")
    public ResponseEntity<List<RefreshTokenDTO>> getRefreshTokens() {
        return new ResponseEntity<>(
                refreshTokenService.loadAllRefreshToken().stream().
                        map(token -> modelMapper.map(token, RefreshTokenDTO.class)).toList(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/registration-tokens")
    public ResponseEntity<List<RegistrationTokenDTO>> getRegistrationTokens() {
        return new ResponseEntity<>(
                registrationTokenService.loadAllRegistrationTokens().stream().
                        map(token -> modelMapper.map(token, RegistrationTokenDTO.class)).toList(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/password-reset-tokens")
    public ResponseEntity<List<PasswordResetTokenDTO>> getPasswordResetTokens() {
        return new ResponseEntity<>(
                passwordResetTokenService.loadAllPasswordResetTokens().stream().
                        map(token -> modelMapper.map(token, PasswordResetTokenDTO.class)).toList(), HttpStatus.OK);
    }

}
