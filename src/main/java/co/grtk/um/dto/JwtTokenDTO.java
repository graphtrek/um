package co.grtk.um.dto;


import lombok.Data;

import java.time.Instant;

@Data
public class JwtTokenDTO {

    private String token;

    private int timePeriodMinutes;

    private Instant issuedAtUtcTime;

    private Instant expiresAtUtcTime;

    private String scope;

    private String subject;

    private String userName;

    private String ip;

    private Instant createdAt;

}
