package co.grtk.um.dto;

import co.grtk.um.model.UmUserStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    //private String roles;
    private UmUserStatus status;
}
