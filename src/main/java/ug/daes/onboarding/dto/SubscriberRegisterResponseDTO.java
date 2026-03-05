
package ug.daes.onboarding.dto;

import java.io.Serializable;

public class SubscriberRegisterResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String suID;

    private String reason;

    private boolean activeSubscriber;

    private String subscriberStatus;

    private SubscriberDetails subscriberDetails;

    private String onboardingPaymentStatus;

    private Boolean firstTimeOnboarding;


    /**
     * Default no-argument constructor.
     * <p>
     * Required for frameworks like Jackson for deserialization.
     * Intentionally empty.
     * </p>
     */
    public SubscriberRegisterResponseDTO() {
        // Intentionally empty
    }

    public String getSuID() {
        return suID;
    }

    public void setSuID(String suID) {
        this.suID = suID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isActiveSubscriber() {
        return activeSubscriber;
    }

    public void setActiveSubscriber(boolean activeSubscriber) {
        this.activeSubscriber = activeSubscriber;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSubscriberStatus() {
        return subscriberStatus;
    }

    public void setSubscriberStatus(String subscriberStatus) {
        this.subscriberStatus = subscriberStatus;
    }

    public SubscriberDetails getSubscriberDetails() {
        return subscriberDetails;
    }

    public void setSubscriberDetails(SubscriberDetails subscriberDetails) {
        this.subscriberDetails = subscriberDetails;
    }

    public String getOnboardingPaymentStatus() {
        return onboardingPaymentStatus;
    }

    public void setOnboardingPaymentStatus(String onboardingPaymentStatus) {
        this.onboardingPaymentStatus = onboardingPaymentStatus;
    }


    public Boolean getFirstTimeOnboarding() {
        return firstTimeOnboarding;
    }

    public void setFirstTimeOnboarding(Boolean firstTimeOnboarding) {
        this.firstTimeOnboarding = firstTimeOnboarding;
    }

    @Override
    public String toString() {
        return "SubscriberRegisterResponseDTO [suID=" + suID + ", reason=" + reason + ", activeSubscriber="
                + activeSubscriber + ", subscriberStatus=" + subscriberStatus + ", subscriberDetails="
                + subscriberDetails + ", onboardingPaymentStatus=" + onboardingPaymentStatus + ", firstTimeOnboarding="
                + firstTimeOnboarding + "]";
    }


}