package co.grtk.um.dto;

import lombok.Data;

@Data
public class TokenRequest {
    String code;
    String refreshToken;
}
