package co.grtk.um.controller;

import co.grtk.um.dto.JwtTokenDTO;
import co.grtk.um.dto.RefreshTokenDTO;
import co.grtk.um.service.JwtTokenService;
import co.grtk.um.service.PasswordResetTokenService;
import co.grtk.um.service.RefreshTokenService;
import co.grtk.um.service.VerificationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class TokensRestController {
    public final JwtTokenService jwtTokenService;
    public final PasswordResetTokenService passwordResetTokenService;
    public final VerificationTokenService verificationTokenService;
    public final RefreshTokenService refreshTokenService;
    public final ModelMapper modelMapper;

    @GetMapping("/api/jwt-tokens")
    public ResponseEntity<List<JwtTokenDTO>> getJwtTokens() {
        return new ResponseEntity<>(
                jwtTokenService.loadAllJwtToken().stream().
                        map(token -> modelMapper.map(token, JwtTokenDTO.class)).toList(), HttpStatus.OK);
    }

    @GetMapping("/api/refresh-tokens")
    public ResponseEntity<List<RefreshTokenDTO>> getRefreshTokens() {
        return new ResponseEntity<>(
                refreshTokenService.loadAllRefreshToken().stream().
                        map(token -> modelMapper.map(token, RefreshTokenDTO.class)).toList(), HttpStatus.OK);
    }

}
