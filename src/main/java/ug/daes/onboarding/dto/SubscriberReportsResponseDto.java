package ug.daes.onboarding.dto;


public class SubscriberReportsResponseDto {


    private String fullName;


    private String onboardingMethod;

    private String idDocNumber;

    private String certificateSerialNumber;

    private String certificateIssueDate;

    private String cerificateExpiryDate;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getIdDocNumber() {
        return idDocNumber;
    }

    public void setIdDocNumber(String idDocNumber) {
        this.idDocNumber = idDocNumber;
    }

    public String getCertificateSerialNumber() {
        return certificateSerialNumber;
    }

    public void setCertificateSerialNumber(String certificateSerialNumber) {
        this.certificateSerialNumber = certificateSerialNumber;
    }

    public String getCertificateIssueDate() {
        return certificateIssueDate;
    }

    public void setCertificateIssueDate(String certificateIssueDate) {
        this.certificateIssueDate = certificateIssueDate;
    }

    public String getCerificateExpiryDate() {
        return cerificateExpiryDate;
    }

    public void setCerificateExpiryDate(String cerificateExpiryDate) {
        this.cerificateExpiryDate = cerificateExpiryDate;
    }

    public String getOnboardingMethod() {
        return onboardingMethod;
    }

    public void setOnboardingMethod(String onboardingMethod) {
        this.onboardingMethod = onboardingMethod;
    }

    @Override
    public String toString() {
        return "SubscriberReportsResponseDto [fullName=" + fullName + ", onboardingMethod=" + onboardingMethod
                + ", idDocNumber=" + idDocNumber + ", certificateSerialNumber=" + certificateSerialNumber
                + ", certificateIssueDate=" + certificateIssueDate + ", cerificateExpiryDate=" + cerificateExpiryDate
                + "]";
    }

}
