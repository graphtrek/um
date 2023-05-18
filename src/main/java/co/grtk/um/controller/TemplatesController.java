package co.grtk.um.controller;


import co.grtk.um.dao.LoginCredentials;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class TemplatesController {

    static final String VIEW_INDEX = "pages/index";
    static final String LOGIN_INDEX = "pages/login";

    static final String REGISTER_INDEX = "pages/register";
    @GetMapping(value = "/")
    public String getHome(Model model) {
        model.addAttribute("title", "");
        return VIEW_INDEX;
    }

    @GetMapping(value = "/login")
    public String getLogin(Model model) {
        model.addAttribute("title", "");
        return LOGIN_INDEX;
    }

    @PostMapping("login")
    String postLogin(@ModelAttribute("LoginCredentials") LoginCredentials loginCredentials, Model model) {
        log.info("POST /login LoginCredentials: {}", loginCredentials);
        model.addAttribute("loginCredentials", loginCredentials);
        return LOGIN_INDEX;
    }

    @GetMapping(value = "/register")
    public String getRegister(Model model) {
        model.addAttribute("title", "");
        return REGISTER_INDEX;
    }

}