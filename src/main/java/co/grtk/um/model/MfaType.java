package co.grtk.um.model;

import lombok.Getter;

@Getter
public enum MfaType {
    NONE("NONE"), EMAIL("EMAIL"), APP("APP"), SMS("SMS");
    private final String code;

    MfaType(String code) {
        this.code = code;
    }
}
