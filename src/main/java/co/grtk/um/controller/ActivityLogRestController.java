package co.grtk.um.controller;

import co.grtk.ual.dto.UserActivityLogDTO;
import co.grtk.um.service.ActivityLogService;
import co.grtk.um.service.KafkaPublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ActivityLogRestController {

    private final ActivityLogService activityLogService;
    private final KafkaPublisherService kafkaPublisherService;

    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/listUserActivity")
    public List<UserActivityLogDTO> listActivityLogs() {
        return activityLogService.listActivityLogs();
    }

    @PostMapping("/api/logUserActivity")
    public void log(@RequestBody UserActivityLogDTO dto) {
        kafkaPublisherService.logUserActivity(dto);
    }
}
