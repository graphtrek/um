package co.grtk.um.controller;

import co.grtk.um.dto.ProfileDTO;
import co.grtk.um.service.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@AllArgsConstructor
@SecurityRequirement(name = "um")
public class ProfileRestController {
    private final ProfileService profileService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/profile")
    public ProfileDTO getProfile(Principal principal) {
        return profileService.loadUser(principal.getName());
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("/api/saveProfile")
    public ProfileDTO saveProfile(Principal principal, @RequestBody ProfileDTO profileDTO) {
        return profileService.saveProfile(principal.getName(), profileDTO);
    }

}
