package co.grtk.um.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class TemplatesController {
    static final String VIEW_INDEX = "pages/index";
    static final String LOGIN_INDEX = "pages/login";
    static final String REGISTER_INDEX = "pages/register";
    static final String VIEW_USERS = "pages/users";
    static final String VIEW_FORGOT_PASSWORD = "pages/forgot-password";
    static final String SECURED = "secured";

    @GetMapping(value = "/")
    public String getHome(Model model) {
        model.addAttribute("page", "home");
        model.addAttribute(SECURED, false);
        return VIEW_INDEX;
    }

    @GetMapping(value = "/login")
    public String getLogin(Model model) {
        model.addAttribute("page", "login");
        model.addAttribute("error", false);
        model.addAttribute(SECURED, false);
        return LOGIN_INDEX;
    }

    @GetMapping(value = "/register")
    public String getRegister(Model model) {
        model.addAttribute("page", "register");
        model.addAttribute(SECURED, false);
        return REGISTER_INDEX;
    }

    @GetMapping(value = "/forgot-password")
    public String getForgotPassword(Model model) {
        model.addAttribute("page", "forgot-password");
        model.addAttribute(SECURED, false);
        return VIEW_FORGOT_PASSWORD;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("page", "users");
        model.addAttribute(SECURED, true);
        return VIEW_USERS;
    }
}