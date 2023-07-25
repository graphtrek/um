package co.grtk.um.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class RefreshTokenDTO {
    private String userName;
    private String userEmail;
    private String token;
    private int timePeriodMinutes;
    private Instant issuedAtUtcTime;
    private Instant expiresAtUtcTime;
    private Instant createdAt;
    private Instant updatedAt;
    private String ip;
}
