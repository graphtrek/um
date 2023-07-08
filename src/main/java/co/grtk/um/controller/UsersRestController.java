package co.grtk.um.controller;

import co.grtk.um.dto.UserDTO;
import co.grtk.um.repository.PrincipalRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@AllArgsConstructor
@RestController
public class UsersRestController {
    public final PrincipalRepository principalRepository;
    public final ModelMapper modelMapper;
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/api/users")
    public UserDTO[] getUsers(Principal principal) {
        return modelMapper.map(principalRepository.findAll(),UserDTO[].class);
    }
}
