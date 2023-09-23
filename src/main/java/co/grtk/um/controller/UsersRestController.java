package co.grtk.um.controller;

import co.grtk.um.dto.UserDTO;
import co.grtk.um.service.UmUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
public class UsersRestController {
    public final UmUserDetailsService umUserDetailsService;
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/users")
    public List<UserDTO> getUsers(Principal principal) {
        return umUserDetailsService.loadAllUsers();
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("/api/saveUser")
    public UserDTO saveUser(@RequestBody UserDTO userDTO) {
        return umUserDetailsService.saveUser(userDTO);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/profile")
    public UserDTO getProfile(Principal principal) {
        return umUserDetailsService.loadUser(principal.getName());
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @PostMapping("/api/saveProfile")
    public UserDTO saveProfile(Principal principal, @RequestBody UserDTO userDTO) {
        return umUserDetailsService.saveProfile(principal.getName(), userDTO);
    }

}
