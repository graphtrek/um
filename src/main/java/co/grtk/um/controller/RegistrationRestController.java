package co.grtk.um.controller;

import co.grtk.um.model.User;
import co.grtk.um.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Slf4j
@RestController
public class RegistrationRestController {
    private final RegistrationService registrationService;
    private final HttpServletRequest servletRequest;


    @PostMapping("/api/registerUser")
    public ResponseEntity<String> register(@RequestBody User user) {
        log.info("registerUser application url: {}", applicationUrl(servletRequest));
        registrationService.registerUser(user);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/api/validateToken")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token){
        log.info("verifyEmail token {}", token);
        registrationService.validateToken(token);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    public String applicationUrl(HttpServletRequest request) {
        return request.getScheme() + "://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();
    }
}
