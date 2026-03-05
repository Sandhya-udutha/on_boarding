package ug.daes.onboarding.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GetSubscriberObDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Subscriber UID is required and cannot be blank.")
    private String suid;

    @NotNull(message = "Selfie required and cannot be blank.")
    private boolean selfieRequired;

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public boolean isSelfieRequired() {
        return selfieRequired;
    }

    public void setSelfieRequired(boolean selfieRequired) {
        this.selfieRequired = selfieRequired;
    }

    @Override
    public String toString() {
        return "GetSubscriberObDataDTO [suid=" + suid + ", selfieRequired=" + selfieRequired + "]";
    }
}
