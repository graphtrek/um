package co.grtk.um.controller;

import co.grtk.um.dto.BeanInitDTO;
import co.grtk.um.service.BootLoggerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "um")
public class UmRestController {

    private final BootLoggerService bootLoggerService;
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/list-beans")
    public List<BeanInitDTO> listBeans() {
        return bootLoggerService.logAllBeansInitializationTime();
    }
}
