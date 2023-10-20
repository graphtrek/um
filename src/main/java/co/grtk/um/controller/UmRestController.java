package co.grtk.um.controller;

import co.grtk.um.dto.BeanInitDTO;
import co.grtk.um.service.LoggerBeanPostProcessor;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "um")
public class UmRestController {

    private final LoggerBeanPostProcessor loggerBeanPostProcessor;
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/list-beans")
    public List<BeanInitDTO> listBeans(@RequestParam(required = false) Integer maxTimeInMillis) {
        if(Objects.isNull(maxTimeInMillis))
            maxTimeInMillis = 0;
        return loggerBeanPostProcessor.logAllBeansInitializationTime(maxTimeInMillis);
    }
}
