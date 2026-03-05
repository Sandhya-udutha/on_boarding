package ug.daes.onboarding.dto;


public class SubscriberDetailsDto {

    private String email;

    private String phoneNo;

    private String subscriberStatus;

    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getSubscriberStatus() {
        return subscriberStatus;
    }

    public void setSubscriberStatus(String subscriberStatus) {
        this.subscriberStatus = subscriberStatus;
    }

    @Override
    public String toString() {
        return "SubscriberDetailsDto{" +
                "email='" + email + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", subscriberStatus='" + subscriberStatus + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }

}

