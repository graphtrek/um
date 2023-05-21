package co.grtk.um.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

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
        return VIEW_INDEX;
    }

    @GetMapping(value = "/login")
    public String getLogin(Model model) {
        model.addAttribute("page", "login");
        return LOGIN_INDEX;
    }

    @GetMapping(value = "/register")
    public String getRegister(Model model) {
        model.addAttribute("page", "register");
        return REGISTER_INDEX;
    }
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/users")
    public String getUsers(Principal principal, Model model) {
        model.addAttribute("page", "users");
        model.addAttribute("userName", principal.getName());
        return VIEW_USERS;
    }
}