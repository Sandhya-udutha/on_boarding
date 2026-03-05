package ug.daes.onboarding.dto;

public class UpdateTemporaryTableDto {

    private String idDocNumber;
    private SubscriberDeviceInfoDto subscriberDeviceInfoDto;
    private String deviceId;
    private String mobileNumber;
    private String emailId;

    public String getIdDocNumber() {
        return idDocNumber;
    }

    public void setIdDocNumber(String idDocNumber) {
        this.idDocNumber = idDocNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public SubscriberDeviceInfoDto getSubscriberDeviceInfoDto() {
        return subscriberDeviceInfoDto;
    }

    public void setSubscriberDeviceInfoDto(SubscriberDeviceInfoDto subscriberDeviceInfoDto) {
        this.subscriberDeviceInfoDto = subscriberDeviceInfoDto;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return "UpdateTemporaryTableDto{" +
                "idDocNumber='" + idDocNumber + '\'' +
                ", subscriberDeviceInfoDto=" + subscriberDeviceInfoDto +
                ", deviceId='" + deviceId + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", emailId='" + emailId + '\'' +
                '}';
    }
}
