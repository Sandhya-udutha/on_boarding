package ug.daes.onboarding.dto;

import java.io.Serializable;

public class UpdateOtpDto implements Serializable {

    private boolean otpMobile;

    private String mobileNumber;
    private boolean otpEmail;

    private String email;

    public boolean isOtpMobile() {
        return otpMobile;
    }

    public void setOtpMobile(boolean otpMobile) {
        this.otpMobile = otpMobile;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isOtpEmail() {
        return otpEmail;
    }

    public void setOtpEmail(boolean otpEmail) {
        this.otpEmail = otpEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UpdateOtpDto{" +
                "otpMobile=" + otpMobile +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", otpEmail=" + otpEmail +
                ", email='" + email + '\'' +
                '}';
    }
}
