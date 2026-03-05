package ug.daes.onboarding.dto;

public class SubscriberDetails {
    private String subscriberName;
    private String onboardingMethod;
    private EditTemplateDTO templateDetails;
    private String certificateStatus;
    private PinStatus pinStatus;

    /**
     * Default no-argument constructor.
     * <p>
     * Required for frameworks like Jackson for deserialization.
     * </p>
     */

    public SubscriberDetails() {
        // Intentionally empty: required for Jackson deserialization
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public String getOnboardingMethod() {
        return onboardingMethod;
    }

    public void setOnboardingMethod(String onboardingMethod) {
        this.onboardingMethod = onboardingMethod;
    }

    public EditTemplateDTO getTemplateDetails() {
        return templateDetails;
    }

    public void setTemplateDetails(EditTemplateDTO templateDetails) {
        this.templateDetails = templateDetails;
    }

    public String getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public PinStatus getPinStatus() {
        return pinStatus;
    }

    public void setPinStatus(PinStatus pinStatus) {
        this.pinStatus = pinStatus;
    }

    @Override
    public String toString() {
        return "SubscriberDetails{" +
                "subscriberName='" + subscriberName + '\'' +
                ", onboardingMethod='" + onboardingMethod + '\'' +
                ", templateDetails=" + templateDetails +
                ", certificateStatus='" + certificateStatus + '\'' +
                ", pinStatus=" + pinStatus +
                '}';
    }
}