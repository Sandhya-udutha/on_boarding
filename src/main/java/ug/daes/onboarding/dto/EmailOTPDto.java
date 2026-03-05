package ug.daes.onboarding.dto;

import java.io.Serializable;

public class EmailOTPDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String emailId;

    private String deviceId;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "EmailOTPDto [emailId=" + emailId + ", deviceId=" + deviceId + "]";
    }
}
