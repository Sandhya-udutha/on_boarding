package ug.daes.onboarding.dto;

import java.io.Serializable;

public class UpdateDto implements Serializable {

    private String suid;

    private boolean updateMobile;

    private String mobileNumber;

    private boolean updateEmail;

    private String email;

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public boolean isUpdateMobile() {
        return updateMobile;
    }

    public void setUpdateMobile(boolean updateMobile) {
        this.updateMobile = updateMobile;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isUpdateEmail() {
        return updateEmail;
    }

    public void setUpdateEmail(boolean updateEmail) {
        this.updateEmail = updateEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UpdateDto{" +
                "suid='" + suid + '\'' +
                ", updateMobile=" + updateMobile +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", updateEmail=" + updateEmail +
                ", email='" + email + '\'' +
                '}';
    }
}
