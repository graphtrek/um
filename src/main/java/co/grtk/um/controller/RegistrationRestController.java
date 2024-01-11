package co.grtk.um.controller;

import co.grtk.um.eventlistener.MailEvent;
import co.grtk.um.eventlistener.MailType;
import co.grtk.um.model.UmUser;
import co.grtk.um.model.RegistrationToken;
import co.grtk.um.service.RegistrationService;
import co.grtk.um.service.RegistrationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static co.grtk.um.controller.UIController.applicationUrl;

@AllArgsConstructor
@Slf4j
@RestController
public class RegistrationRestController {
    private final RegistrationService registrationService;
    private final RegistrationTokenService registrationTokenService;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/api/registerUser")
    public ResponseEntity<String> register(@RequestBody UmUser umUser, final HttpServletRequest request) {
        log.info("registerUser application url: {}", applicationUrl(request));
        RegistrationToken registrationToken = registrationService.registerUser(umUser);
        publisher.publishEvent(
                new MailEvent(
                        MailType.REGISTRATION,
                        registrationToken.getUmUser(),
                        applicationUrl(request) + "/api/validateRegistrationToken?token="+ registrationToken.getToken()));
        return new ResponseEntity<>(registrationToken.getToken(), HttpStatus.OK);
    }

    @GetMapping("/api/resendRegistrationToken")
    public ResponseEntity<String> resendRegistrationToken(@RequestParam("token") String oldToken, final HttpServletRequest request) {
        log.info("resendToken application url: {}", applicationUrl(request));
        RegistrationToken registrationToken = registrationTokenService.generateNewRegistrationToken(oldToken);
        publisher.publishEvent(
                new MailEvent(
                    MailType.REGISTRATION,
                    registrationToken.getUmUser(),
                        applicationUrl(request) + "/api/validateRegistrationToken?token="+ registrationToken.getToken()));
        return new ResponseEntity<>(registrationToken.getToken(), HttpStatus.OK);
    }

}