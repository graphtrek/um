package co.grtk.um.controller;

import co.grtk.um.dto.UserDTO;
import co.grtk.um.model.UmUser;
import co.grtk.um.repository.UmUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@AllArgsConstructor
@RestController
public class UsersRestController {
    public final UmUserRepository umUserRepository;
    public final ModelMapper modelMapper;
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/users")
    public UserDTO[] getUsers(Principal principal) {
        return modelMapper.map(umUserRepository.findAll(),UserDTO[].class);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("/api/saveUser")
    public UserDTO saveUser(@RequestBody UmUser umUser) {
        return modelMapper.map(umUser,UserDTO.class);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/api/profile")
    public UserDTO getProfile(Principal principal) {
        UmUser user =
                umUserRepository.findByEmail(principal.getName()).
                        orElseThrow(() -> new UsernameNotFoundException("User not found email: " + principal.getName()));
        return modelMapper.map(user,UserDTO.class);
    }
}
