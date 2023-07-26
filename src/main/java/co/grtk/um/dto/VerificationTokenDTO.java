package co.grtk.um.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class VerificationTokenDTO {

    private String token;

    private String userName;

    private String userEmail;

    private int timePeriodMinutes;

    private Instant issuedAtUtcTime;

    private Instant expiresAtUtcTime;

    private Instant createdAt;

    private Instant updatedAt;

}
