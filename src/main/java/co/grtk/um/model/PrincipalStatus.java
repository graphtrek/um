package co.grtk.um.model;

import lombok.Getter;

@Getter
public enum PrincipalStatus {
    PENDING("PENDING"), REGISTERED("REGISTERED"), DELETED("DELETED");
    private final String code;
    PrincipalStatus(String code) {
        this.code = code;
    }
}
