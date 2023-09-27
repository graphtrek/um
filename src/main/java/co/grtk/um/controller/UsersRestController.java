package co.grtk.um.controller;

import co.grtk.um.dto.UserDTO;
import co.grtk.um.service.UmUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
public class UsersRestController {
    public final UmUserService umUserService;
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/users")
    public List<UserDTO> getUsers() {
        return umUserService.loadAllUsers();
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("/api/saveUser")
    public UserDTO saveUser(@RequestBody UserDTO userDTO) {
        return umUserService.saveUser(userDTO);
    }

}
