package co.grtk.um.dto;

import co.grtk.um.model.MfaType;
import co.grtk.um.model.UmUserStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private MfaType mfaType;
    private UmUserStatus status;
    private String roles;
}
