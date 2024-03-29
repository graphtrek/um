package co.grtk.ual.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserActivityLogDTO {
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
    private String text;
    private int elapsed;
}
