package co.grtk.um.controller;

import co.grtk.um.listener.MailEvent;
import co.grtk.um.listener.MailType;
import co.grtk.um.model.User;
import co.grtk.um.model.VerificationToken;
import co.grtk.um.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static co.grtk.um.controller.TemplatesController.applicationUrl;

@AllArgsConstructor
@Slf4j
@RestController
public class RegistrationRestController {
    private final RegistrationService registrationService;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/api/registerUser")
    public ResponseEntity<String> register(@RequestBody User user,final HttpServletRequest request) {
        log.info("registerUser application url: {}", applicationUrl(request));
        VerificationToken verificationToken = registrationService.registerUser(user);
        publisher.publishEvent(
                new MailEvent(
                        MailType.REGISTRATION,
                        verificationToken.getUser(),
                        applicationUrl(request) + "/api/validateToken?token="+verificationToken.getToken()));
        return new ResponseEntity<>(verificationToken.getToken(), HttpStatus.OK);
    }

    @GetMapping("/api/resendToken")
    public ResponseEntity<String> resendToken(@RequestParam("token") String oldToken,final HttpServletRequest request) {
        log.info("resendToken application url: {}", applicationUrl(request));
        VerificationToken verificationToken = registrationService.generateNewVerificationToken(oldToken);
        publisher.publishEvent(
                new MailEvent(
                    MailType.RESEND_TOKEN,
                    verificationToken.getUser(),
                        applicationUrl(request) + "/api/validateToken?token="+verificationToken.getToken()));
        return new ResponseEntity<>(verificationToken.getToken(), HttpStatus.OK);
    }

}