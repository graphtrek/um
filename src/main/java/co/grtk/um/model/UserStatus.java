package co.grtk.um.model;

import lombok.Getter;

@Getter
public enum UserStatus {
    PENDING("PENDING"), REGISTERED("REGISTERED"), DELETED("DELETED");
    private final String code;
    UserStatus(String code) {
        this.code = code;
    }
}
