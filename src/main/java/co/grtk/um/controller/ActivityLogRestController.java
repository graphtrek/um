package co.grtk.um.controller;

import co.grtk.ual.dto.UserActivityLogDTO;
import co.grtk.ual.dto.UserActivityLogRequestDTO;
import co.grtk.um.service.ActivityLogService;
import co.grtk.um.service.KafkaPublisherService;
import co.grtk.um.service.RequestContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ActivityLogRestController {

    private final ActivityLogService activityLogService;
    private final KafkaPublisherService kafkaPublisherService;
    private final RequestContextService requestContextService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/listUserActivity")
    public List<UserActivityLogDTO> listActivityLogs(Authentication authentication, @ModelAttribute UserActivityLogRequestDTO dto) {

        return activityLogService.listActivityLogs(authentication.getName());
    }

    @PostMapping("/api/logUserActivity")
    public void log(@RequestBody UserActivityLogDTO dto) {
        kafkaPublisherService.logUserActivity(dto);
    }
}
