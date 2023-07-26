package co.grtk.um.controller;

import co.grtk.um.exception.InvalidPasswordResetTokenException;
import co.grtk.um.exception.InvalidVerificationTokenException;
import co.grtk.um.service.PasswordResetTokenService;
import co.grtk.um.service.VerificationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import static co.grtk.um.controller.TemplatesController.*;

@AllArgsConstructor
@Slf4j
@Controller
public class RegistrationController {
    private final VerificationTokenService verificationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private static final String ERROR = "error";


    @GetMapping("/api/validateToken")
    public String validateToken(@ModelAttribute("token") String token, Model model) {
        log.info("validateToken token {}", token);
        model.addAttribute("page", "login");
        model.addAttribute("secured", false);
        model.addAttribute("token", token);
        model.addAttribute(ERROR, false);
        try {
            verificationTokenService.validateToken(token);
        } catch (InvalidVerificationTokenException e) {
            log.error("InvalidVerificationToken :" + token);
            model.addAttribute(ERROR, true);
        }
        return LOGIN_INDEX;
    }

    @GetMapping("/api/validatePasswordResetToken")
    public String validatePasswordResetToken(@ModelAttribute("token") String token, Model model) {
        log.info("validatePasswordResetToken token {}", token);
        model.addAttribute("page", "login");
        model.addAttribute("secured", false);
        model.addAttribute("token", token);
        model.addAttribute(ERROR, false);
        try {
            passwordResetTokenService.validatePasswordResetToken(token);
        } catch (InvalidPasswordResetTokenException e) {
            log.error("InvalidPasswordResetTokenException :" + token);
            model.addAttribute(ERROR, true);
            return VIEW_FORGOT_PASSWORD;
        }
        return VIEW_CHANGE_PASSWORD;
    }

}
