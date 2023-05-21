package co.grtk.um.controller;

import co.grtk.um.model.User;
import co.grtk.um.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@AllArgsConstructor
@RestController
public class UsersRestController {
    public final UserRepository userRepository;

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/api/users")
    public Iterable<User> getUsers(Principal principal) {
        return userRepository.findAll();
    }
}
