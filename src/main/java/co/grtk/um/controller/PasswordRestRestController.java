package co.grtk.um.controller;

import co.grtk.um.dto.PasswordResetRequest;
import co.grtk.um.exception.UserNotFoundException;
import co.grtk.um.listener.MailEvent;
import co.grtk.um.listener.MailType;
import co.grtk.um.model.PasswordResetToken;
import co.grtk.um.model.UmUser;
import co.grtk.um.repository.UmUserRepository;
import co.grtk.um.service.PasswordResetTokenService;
import co.grtk.um.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static co.grtk.um.controller.UIController.applicationUrl;

@Slf4j
@AllArgsConstructor
@RestController
public class PasswordRestRestController {
    private final UmUserRepository umUserRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final ApplicationEventPublisher publisher;
    private final RegistrationService registrationService;
    @PostMapping("/api/forgotPassword")
    public ResponseEntity<String> passwordResetEmail(@RequestBody String email, HttpServletRequest request) {
        log.info("forgotPassword email: {} applicationUrl: {}", email, applicationUrl(request));
        UmUser umUser = umUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(("User Not Found")));
        String passwordResetToken = UUID.randomUUID().toString();
        PasswordResetToken passwordRestToken = passwordResetTokenService.createPasswordResetTokenForUser(umUser,passwordResetToken);
        publisher.publishEvent(
                new MailEvent(
                        MailType.PASSWORD_RESET,
                        passwordRestToken.getUmUser(),
                        applicationUrl(request) + "/api/validatePasswordResetToken?token="+passwordRestToken.getToken()));
        return new ResponseEntity<>(passwordRestToken.getToken(), HttpStatus.OK);
    }

    @GetMapping("/api/resendPasswordResetToken")
    public ResponseEntity<String> resendPasswordToken(@RequestParam("token") String oldToken, final HttpServletRequest request) {
        log.info("resendPasswordResetToken oldToken: {} applicationUrl: {}",oldToken, applicationUrl(request));
        PasswordResetToken passwordRestToken = passwordResetTokenService.generateNewPasswordResetTokenFor(oldToken);
        publisher.publishEvent(
                new MailEvent(
                        MailType.PASSWORD_RESET,
                        passwordRestToken.getUmUser(),
                        applicationUrl(request) + "/api/validatePasswordResetToken?token="+passwordRestToken.getToken()));
        return new ResponseEntity<>(passwordRestToken.getToken(), HttpStatus.OK);
    }

    @PostMapping("/api/resetPassword")
    public ResponseEntity<String> register(@RequestBody PasswordResetRequest passwordResetRequest, final HttpServletRequest request) {
        log.info("resetPassword application passwordResetRequest: {}", passwordResetRequest);
        passwordResetTokenService.validatePasswordResetToken(passwordResetRequest.getToken());
        UmUser umUser = passwordResetTokenService.findPasswordResetToken(passwordResetRequest.getToken()).getUmUser();
        registrationService.resetPassword(umUser,passwordResetRequest.getPassword());
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
