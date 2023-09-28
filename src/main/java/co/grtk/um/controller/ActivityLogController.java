package co.grtk.um.controller;

import co.grtk.um.dto.UserActivityLogDTO;
import co.grtk.um.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ActivityLogController {

    private final ActivityLogService activityLogService;
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/api/logs")
    public List<UserActivityLogDTO> listActivityLogs() {
        return activityLogService.listActivityLogs();
    }
}
