package co.grtk.um.controller;

import co.grtk.um.dto.UserDTO;
import co.grtk.um.model.UmUser;
import co.grtk.um.service.UmUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@AllArgsConstructor
@RestController
public class UsersRestController {
    public final UmUserDetailsService umUserDetailsService;
    public final ModelMapper modelMapper;
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/users")
    public UserDTO[] getUsers(Principal principal) {
        return modelMapper.map(umUserDetailsService.loadAllUsers(),UserDTO[].class);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("/api/saveUser")
    public UserDTO saveUser(@RequestBody UserDTO userDTO) {
        UmUser umUser = umUserDetailsService.saveUser(modelMapper.map(userDTO, UmUser.class));
        return modelMapper.map(umUser,UserDTO.class);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/api/profile")
    public UserDTO getProfile(Principal principal) {
        UmUser user = umUserDetailsService.loadUserByEmail(principal.getName());
        return modelMapper.map(user,UserDTO.class);
    }
}
