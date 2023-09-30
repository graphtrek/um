package co.grtk.um.service;

import co.grtk.ual.dto.UserActivityLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ActivityLogService {
    public final RestClient activityLogClient;

    public List<UserActivityLogDTO> listActivityLogs() {
        return activityLogClient.get()
                .uri("/listUserActivity")
                .header("Authorization","Basic X2VzbF9pbnRlcm5hbF93czpfZXNsX2ludGVybmFsX3dz")
                .retrieve()
                .body(new ParameterizedTypeReference<List<UserActivityLogDTO>>() {});
    }

}
