package co.grtk.um.controller;

import co.grtk.um.exception.InvalidVerificationTokenException;
import co.grtk.um.model.User;
import co.grtk.um.service.RegistrationService;
import co.grtk.um.service.VerificationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static co.grtk.um.controller.TemplatesController.LOGIN_INDEX;
import static co.grtk.um.controller.TemplatesController.REGISTER_INDEX;

@AllArgsConstructor
@Slf4j
@Controller
public class RegistrationController {
    private final RegistrationService registrationService;
    private final VerificationTokenService verificationTokenService;

    @PostMapping("/api/registerUserForm")
    public String postRegister(@ModelAttribute("User") User user, Model model) {
        log.info("POST /api/registerUserForm user: {}", user);
        registrationService.registerUser(user);
        return REGISTER_INDEX;
    }
    @GetMapping("/api/validateToken")
    public String validateToken(@ModelAttribute("token") String token, Model model){
        log.info("validateToken token {}", token);
        model.addAttribute("page", "login");
        model.addAttribute("secured",false);
        model.addAttribute("token", token);
        model.addAttribute("error", false);
        try {
            verificationTokenService.validateToken(token);
        } catch (InvalidVerificationTokenException e) {
            log.error("InvalidVerificationToken :" + token);
            model.addAttribute("error", true);
        }
        return LOGIN_INDEX;
    }

}
