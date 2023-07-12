package co.grtk.um.model;

import lombok.Getter;

@Getter
public enum UmUserStatus {
    PENDING("PENDING"), REGISTERED("REGISTERED"), LOCKED("LOCKED"), DELETED("DELETED");
    private final String code;
    UmUserStatus(String code) {
        this.code = code;
    }
}
