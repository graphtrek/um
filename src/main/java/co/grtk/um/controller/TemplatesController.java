package co.grtk.um.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@Controller
public class TemplatesController {
    static final String VIEW_INDEX = "pages/index";
    static final String LOGIN_INDEX = "pages/login";
    static final String REGISTER_INDEX = "pages/register";
    static final String VIEW_USERS = "pages/users";
    static final String VIEW_FORGOT_PASSWORD = "pages/forgot-password";
    static final String VIEW_CHANGE_PASSWORD = "pages/change-password";
    static final String SECURED = "secured";
    static final String PAGE = "page";

    @GetMapping(value = "/")
    public String getHome(Model model) {
        model.addAttribute(PAGE, "home");
        model.addAttribute(SECURED, false);
        return VIEW_INDEX;
    }

    @GetMapping(value = "/login")
    public String getLogin(Model model) {
        model.addAttribute(PAGE, "login");
        model.addAttribute("error", false);
        model.addAttribute(SECURED, false);
        return LOGIN_INDEX;
    }

    @GetMapping(value = "/register")
    public String getRegister(Model model) {
        model.addAttribute(PAGE, "register");
        model.addAttribute(SECURED, false);
        return REGISTER_INDEX;
    }

    @GetMapping(value = "/forgot-password")
    public String getForgotPassword(Model model) {
        model.addAttribute(PAGE, "forgot-password");
        model.addAttribute(SECURED, false);
        return VIEW_FORGOT_PASSWORD;
    }

    @GetMapping(value = "/change-password")
    public String getChangePassword(@ModelAttribute("token") String token, Model model) {
        model.addAttribute(PAGE, "change-password");
        model.addAttribute("token", token);
        model.addAttribute(SECURED, false);
        return VIEW_CHANGE_PASSWORD;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute(PAGE, "users");
        model.addAttribute(SECURED, true);
        return VIEW_USERS;
    }

    public static String applicationUrl(HttpServletRequest request) {
        return request.getScheme() + "://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();
    }

}