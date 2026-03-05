package ug.daes.onboarding.dto;

public class VisitorCardRequestDTO {

    private String fullName;

    private String nationality;

    private String dateOfBirth;

    private String visitorCardNumber;

    private String suid;

    private String idDocNumber;

    private String selfieUri;


    private String gender;

    private String subscriberType;

    public String getSubscriberType() {
        return subscriberType;
    }

    public void setSubscriberType(String subscriberType) {
        this.subscriberType = subscriberType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getVisitorCardNumber() {
        return visitorCardNumber;
    }

    public void setVisitorCardNumber(String visitorCardNumber) {
        this.visitorCardNumber = visitorCardNumber;
    }

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSelfieUri(String selfieUri) {
        this.selfieUri = selfieUri;
    }

    @Override
    public String toString() {
        return "VisitorCardRequestDTO{" +
                "fullName='" + fullName + '\'' +
                ", nationality='" + nationality + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", visitorCardNumber='" + visitorCardNumber + '\'' +
                ", suid='" + suid + '\'' +
                ", idDocNumber='" + idDocNumber + '\'' +
                ", selfieUri='" + selfieUri + '\'' +
                ", gender='" + gender + '\'' +
                ", subscriberType='" + subscriberType + '\'' +
                '}';
    }
}
