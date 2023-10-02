package co.grtk.ual.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserActivityLogRequestDTO {
    private LocalDateTime fromTs;
    private LocalDateTime toTs;
}
