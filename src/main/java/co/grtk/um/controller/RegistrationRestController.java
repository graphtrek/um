package co.grtk.um.controller;

import co.grtk.um.model.User;
import co.grtk.um.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@RestController
public class RegistrationRestController {
    private final RegistrationService registrationService;
    private final HttpServletRequest servletRequest;
    @PostMapping("/api/registerUser")
    public void register(@RequestBody User user) {
        log.info("Application url: {}", applicationUrl(servletRequest));
        registrationService.registerUser(user);
    }

    public String applicationUrl(HttpServletRequest request) {
        return request.getScheme() + "://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();
    }
}
