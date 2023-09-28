package co.grtk.um.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class UserActivityLogDTO {
    private String id;
    private String eventId;
    private String token;
    private String clientId;
    private String appId;
    private String category;
    private String correlationId;
    private String textParams;
    private LocalDateTime timeStamp;
    private String activityCode;
    private String resultCode;
    private String logLevel;
}
