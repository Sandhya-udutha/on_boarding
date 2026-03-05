package ug.daes.onboarding.dto;

import java.io.Serializable;

public class SmsDTO implements Serializable {

    private String phoneNumber;

    private String smsText;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    @Override
    public String toString() {
        return "SmsDTO [phoneNumber=" + phoneNumber + ", smsText=" + smsText + "]";
    }

}
