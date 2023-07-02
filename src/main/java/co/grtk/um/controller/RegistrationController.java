package co.grtk.um.controller;

import co.grtk.um.exception.InvalidVerificationTokenException;
import co.grtk.um.model.User;
import co.grtk.um.service.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static co.grtk.um.controller.TemplatesController.REGISTER_INDEX;

@AllArgsConstructor
@Slf4j
@Controller
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/api/registerUserForm")
    String postRegister(@ModelAttribute("User") User user, Model model) {
        log.info("POST /api/registerUserForm user: {}", user);
        registrationService.registerUser(user);
        return REGISTER_INDEX;
    }
    @GetMapping("/api/validateToken")
    public ModelAndView validateToken(@RequestParam("token") String token, ModelMap model){
        log.info("validateToken token {}", token);
        model.put("secured",false);
        model.put("token", token);
        try {
            registrationService.validateToken(token);
        } catch (InvalidVerificationTokenException e) {
            model.put("error", true);
        }
        return new ModelAndView("redirect:/login" , model);
    }

}
