package co.grtk.um.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class LoginCredentials {

    public String email;
    public String password;

}
