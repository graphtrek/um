package co.grtk.um.dto;

import co.grtk.um.model.MfaType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProfileDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private MfaType mfaType;
}
