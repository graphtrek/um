package co.grtk.um.controller;

import co.grtk.ual.dto.UserActivityLogDTO;
import co.grtk.ual.dto.UserActivityLogRequestDTO;
import co.grtk.um.service.ActivityLogService;
import co.grtk.um.service.KafkaPublisherService;
import co.grtk.um.service.RequestContextService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "um")
@ConditionalOnProperty(name = "application.kafka.enabled", havingValue = "true")
public class ActivityLogRestController {

    private final ActivityLogService activityLogService;
    private final KafkaPublisherService kafkaPublisherService;
    private final RequestContextService requestContextService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/listUserActivity")
    public List<UserActivityLogDTO> listActivityLogs(Authentication authentication, @ModelAttribute UserActivityLogRequestDTO dto) {

        String fromTs =
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now().minusDays(60));
        String toTs =
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());

        return activityLogService.listActivityLogs(authentication.getName(),fromTs,toTs);
    }

    @PostMapping("/api/logUserActivity")
    public void log(@RequestBody UserActivityLogDTO dto) {
        kafkaPublisherService.logUserActivity(dto);
    }
}
