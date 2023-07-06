package co.grtk.um.controller;

import co.grtk.um.exception.UserNotFoundException;
import co.grtk.um.model.PasswordResetToken;
import co.grtk.um.model.User;
import co.grtk.um.repository.UserRepository;
import co.grtk.um.service.PasswordResetTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static co.grtk.um.controller.TemplatesController.applicationUrl;

@Slf4j
@AllArgsConstructor
@RestController
public class PasswordRestRestController {
    private final UserRepository userRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    @PostMapping("/api/forgotPassword")
    public ResponseEntity<String> passwordResetEmail(@RequestBody String email, HttpServletRequest request) {
        log.info("forgotPassword email: {} applicationUrl: {}", email, applicationUrl(request));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(("User Not Found")));
        String passwordResetToken = UUID.randomUUID().toString();
        PasswordResetToken passwordRestToken = passwordResetTokenService.createPasswordResetTokenForUser(user,passwordResetToken);
        return new ResponseEntity<>(passwordRestToken.getToken(), HttpStatus.OK);
    }

    @GetMapping("/api/resendPasswordResetToken")
    public ResponseEntity<String> resendPasswordToken(@RequestParam("token") String oldToken, final HttpServletRequest request) {
        log.info("resendPasswordResetToken oldToken: {} applicationUrl: {}",oldToken, applicationUrl(request));
        PasswordResetToken passwordRestToken = passwordResetTokenService.generateNewPasswordResetTokenFor(oldToken);
        return new ResponseEntity<>(passwordRestToken.getToken(), HttpStatus.OK);
    }

}
