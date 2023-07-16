package co.grtk.um.dto;


import lombok.Data;

@Data
public class TokenResponse {
    String accessToken;
    String qrCode;
}
