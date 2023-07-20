package co.grtk.um.dto;


import lombok.Data;

@Data
public class TokenResponse {
    String accessToken;
    String refreshToken;
    String qrCode;
    String userName;
    String email;
    String scope;

}
