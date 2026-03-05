package ug.daes.onboarding.dto;

public class NewDeviceDTO {
    private boolean newDevice;
    private SubscriberDetailsReponseDTO subscriberStatusDetails;
    private String idDocNumber;
    private String selfieUri;
    private String email;
    private String mobileNumber;

    public boolean isNewDevice() {
        return newDevice;
    }

    public SubscriberDetailsReponseDTO getSubscriberStatusDetails() {
        return subscriberStatusDetails;
    }

    public void setSubscriberStatusDetails(SubscriberDetailsReponseDTO subscriberStatusDetails) {
        this.subscriberStatusDetails = subscriberStatusDetails;
    }

    public String getIdDocNumber() {
        return idDocNumber;
    }

    public void setIdDocNumber(String idDocNumber) {
        this.idDocNumber = idDocNumber;
    }

    public String getSelfieUri() {
        return selfieUri;
    }

    public void setSelfieUri(String selfieUri) {
        this.selfieUri = selfieUri;
    }

    public NewDeviceDTO() {
    }

    public NewDeviceDTO(boolean newDevice) {
        this.newDevice = newDevice;
    }

    public void setNewDevice(boolean newDevice) {
        this.newDevice = newDevice;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "NewDeviceDTO [newDevice=" + newDevice + ", subscriberStatusDetails=" + subscriberStatusDetails
                + ", idDocNumber=" + idDocNumber + ", selfieUri=" + selfieUri + ", email=" + email + ", mobileNumber="
                + mobileNumber + "]";
    }
}
