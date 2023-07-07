package co.grtk.um.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author Sampson Alfred
 */
@Data
@ToString
public class PasswordResetRequest {
    private String token;
    private String password;
    private String repeatPassword;
}
