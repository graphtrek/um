package co.grtk.um.service;

import co.grtk.ual.dto.UserActivityLogDTO;
import co.grtk.ual.dto.UserActivityLogRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ActivityLogService {
    public final RestClient activityLogClient;

    public List<UserActivityLogDTO> listActivityLogs( UserActivityLogRequestDTO dto) {

        String fromTs = dto.getFromTs() != null ?
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dto.getFromTs()) :
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now().minusDays(30));
        String toTs = dto.getToTs() != null ?
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dto.getToTs()) :
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());

        //.uri("/listUserActivity?fromTs={fromTs}&toTs={toTs}","2023-01-01T08:12:01.29","2023-01-01T08:12:01.29")

        return activityLogClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/listUserActivity")
                        .queryParam("fromTs", fromTs)
                        .queryParam("toTs", toTs)
                        .build())
                .header("Authorization","Basic X2VzbF9pbnRlcm5hbF93czpfZXNsX2ludGVybmFsX3dz")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<List<UserActivityLogDTO>>() {});
    }

}
