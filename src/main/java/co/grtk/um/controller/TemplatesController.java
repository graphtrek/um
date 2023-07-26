package co.grtk.um.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
    static final String VIEW_JWT_TOKENS = "pages/jwt-tokens";
    static final String VIEW_REGISTRATION_TOKENS = "pages/verification-tokens";
    static final String VIEW_PASSWORD_RESET_TOKENS = "pages/password-reset-tokens";
    static final String VIEW_REFRESH_TOKENS = "pages/refresh-tokens";
    static final String VIEW_PROFILE = "pages/profile";
    static final String VIEW_NAVBAR = "fragments/navbar";
    static final String VIEW_FORGOT_PASSWORD = "pages/forgot-password";
    static final String VIEW_CHANGE_PASSWORD = "pages/change-password";
    static final String SECURED = "secured";
    static final String ERROR = "error";
    static final String PAGE = "page";
    static final String ADMIN = "admin";

    @GetMapping(value = "/")
    public String getHome(Model model) {
        model.addAttribute(PAGE, "home");
        model.addAttribute(ERROR, false);
        model.addAttribute(SECURED, false);
        return VIEW_INDEX;
    }

    @GetMapping(value = "/login")
    public String getLogin(Model model) {
        model.addAttribute(PAGE, "login");
        model.addAttribute(ERROR, false);
        model.addAttribute(SECURED, false);
        return LOGIN_INDEX;
    }

    @GetMapping(value = "/register")
    public String getRegister(Model model) {
        model.addAttribute(PAGE, "register");
        model.addAttribute(ERROR, false);
        model.addAttribute(SECURED, false);
        return REGISTER_INDEX;
    }

    @GetMapping(value = "/forgot-password")
    public String getForgotPassword(Model model) {
        model.addAttribute(PAGE, "forgot-password");
        model.addAttribute(ERROR, false);
        model.addAttribute(SECURED, false);
        return VIEW_FORGOT_PASSWORD;
    }

    @GetMapping(value = "/change-password")
    public String getChangePassword(@ModelAttribute("token") String token, Model model) {
        model.addAttribute(PAGE, "change-password");
        model.addAttribute(ERROR, false);
        model.addAttribute("token", token);
        model.addAttribute(SECURED, false);
        return VIEW_CHANGE_PASSWORD;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute(PAGE, "users");
        model.addAttribute(ERROR, false);
        model.addAttribute(SECURED, true);
        return VIEW_USERS;
    }

    @GetMapping("/jwt-tokens")
    public String getJwtTokens(Model model) {
        model.addAttribute(PAGE, "jwt-tokens");
        model.addAttribute(ERROR, false);
        model.addAttribute(SECURED, true);
        return VIEW_JWT_TOKENS;
    }

    @GetMapping("/verification-tokens")
    public String getVerificationTokens(Authentication authentication, Model model) {
        model.addAttribute(PAGE, "verification-tokens");
        model.addAttribute(ERROR, false);
        model.addAttribute(SECURED, true);
        return VIEW_REGISTRATION_TOKENS;
    }

    @GetMapping("/password-reset-tokens")
    public String getPasswordResetTokens(Authentication authentication, Model model) {
        model.addAttribute(PAGE, "password-reset-tokens");
        model.addAttribute(ERROR, false);
        model.addAttribute(SECURED, true);
        return VIEW_PASSWORD_RESET_TOKENS;
    }

    @GetMapping("/refresh-tokens")
    public String getRefreshTokens(Authentication authentication, Model model) {
        model.addAttribute(PAGE, "refresh-tokens");
        model.addAttribute(ERROR, false);
        model.addAttribute(SECURED, true);
        return VIEW_REFRESH_TOKENS;
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        model.addAttribute(PAGE, "profile");
        model.addAttribute(ERROR, false);
        model.addAttribute(SECURED, true);
        return VIEW_PROFILE;
    }

    @GetMapping("/navbar")
    public String getNavbar(Authentication authentication, Model model) {
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase().contains("ADMIN"));
        model.addAttribute(ERROR, false);
        model.addAttribute(PAGE, "navbar");
        model.addAttribute(SECURED, true);
        model.addAttribute(ADMIN, isAdmin);
        return VIEW_NAVBAR;
    }

    public static String applicationUrl(HttpServletRequest request) {
        return request.getScheme() + "://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();
    }

}