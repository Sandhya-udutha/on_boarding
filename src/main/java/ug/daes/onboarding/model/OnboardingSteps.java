/**
 *
 */
package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "onboarding_steps")
@NamedQuery(name = "OnboardingSteps.findAll", query = "SELECT o FROM OnboardingSteps o")
public class OnboardingSteps implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "onboarding_step_id")
    private int onboardingStepId;

    @Column(name = "onboarding_step")
    private String onboardingStep;

    @Column(name = "onboarding_step_display_name")
    private String onboardingStepDisplayName;

    @Column(name = "integration_url")
    private String integrationUrl;

    @Column(name = "onboarding_step_threshold")
    private String onboardingStepThreshold;

    @Column(name = "android_tflite_threshold")
    private String andriodTFliteThreshold;

    @Column(name = "andriod_dtt_threshold")
    private String andriodDTTThreshold;

    @Column(name = "ios_tflite_threshold")
    private String iosTFliteThreshold;

    @Column(name = "ios_dtt_threshold")
    private String iosDTTThreshold;


    public int getOnboardingStepId() {
        return onboardingStepId;
    }


    public void setOnboardingStepId(int onboardingStepId) {
        this.onboardingStepId = onboardingStepId;
    }


    public String getOnboardingStep() {
        return onboardingStep;
    }


    public void setOnboardingStep(String onboardingStep) {
        this.onboardingStep = onboardingStep;
    }


    public String getOnboardingStepDisplayName() {
        return onboardingStepDisplayName;
    }


    public void setOnboardingStepDisplayName(String onboardingStepDisplayName) {
        this.onboardingStepDisplayName = onboardingStepDisplayName;
    }


    public String getIntegrationUrl() {
        return integrationUrl;
    }


    public void setIntegrationUrl(String integrationUrl) {
        this.integrationUrl = integrationUrl;
    }


    public String getOnboardingStepThreshold() {
        return onboardingStepThreshold;
    }


    public void setOnboardingStepThreshold(String onboardingStepThreshold) {
        this.onboardingStepThreshold = onboardingStepThreshold;
    }


    public String getAndriodTFliteThreshold() {
        return andriodTFliteThreshold;
    }


    public void setAndriodTFliteThreshold(String andriodTFliteThreshold) {
        this.andriodTFliteThreshold = andriodTFliteThreshold;
    }


    public String getAndriodDTTThreshold() {
        return andriodDTTThreshold;
    }


    public void setAndriodDTTThreshold(String andriodDTTThreshold) {
        this.andriodDTTThreshold = andriodDTTThreshold;
    }


    public String getIosTFliteThreshold() {
        return iosTFliteThreshold;
    }


    public void setIosTFliteThreshold(String iosTFliteThreshold) {
        this.iosTFliteThreshold = iosTFliteThreshold;
    }


    public String getIosDTTThreshold() {
        return iosDTTThreshold;
    }


    public void setIosDTTThreshold(String iosDTTThreshold) {
        this.iosDTTThreshold = iosDTTThreshold;
    }


    @Override
    public String toString() {
        return "OnboardingSteps [onboardingStepId=" + onboardingStepId + ", onboardingStep=" + onboardingStep
                + ", onboardingStepDisplayName=" + onboardingStepDisplayName + ", integrationUrl=" + integrationUrl
                + ", onboardingStepThreshold=" + onboardingStepThreshold + ", andriodTFliteThreshold="
                + andriodTFliteThreshold + ", andriodDTTThreshold=" + andriodDTTThreshold + ", iosTFliteThreshold="
                + iosTFliteThreshold + ", iosDTTThreshold=" + iosDTTThreshold + "]";
    }


}
