package co.grtk.um.controller;

import co.grtk.um.model.User;
import co.grtk.um.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class RegistrationRestController {
    private final RegistrationService registrationService;

    @PostMapping("/api/registerUser")
    public void register(@RequestBody User user) {
        registrationService.registerUser(user);
    }

}
