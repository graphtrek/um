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
    @GetMapping(value = "/")
    public String getHome(Model model) {
        model.addAttribute("page", "home");
        model.addAttribute("secured", false);
        return VIEW_INDEX;
    }

    @GetMapping(value = "/login")
    public String getLogin(Model model) {
        model.addAttribute("page", "login");
        model.addAttribute("secured", false);
        return LOGIN_INDEX;
    }

    @GetMapping(value = "/register")
    public String getRegister(Model model) {
        model.addAttribute("page", "register");
        model.addAttribute("secured", false);
        return REGISTER_INDEX;
    }
    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("page", "users");
        model.addAttribute("secured", true);
        return VIEW_USERS;
    }
}