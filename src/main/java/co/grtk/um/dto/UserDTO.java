package co.grtk.um.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDTO {
    private String name;
    private String email;
    private String roles;
}
