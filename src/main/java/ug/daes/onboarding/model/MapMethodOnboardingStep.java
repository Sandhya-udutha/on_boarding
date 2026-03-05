/**
 *
 */
package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "map_method_onboarding_steps")
@NamedQuery(name = "MapMethodOnboardingStep.findAll", query = "SELECT m FROM MapMethodOnboardingStep m")
public class MapMethodOnboardingStep implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_method_onboarding_step_id")
    private int onboardingStepId;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "template_id")
    private int templateId;

    @Column(name = "onboarding_step")
    private String onboardingStep;

    @Column(name = "sequence")
    private int sequence;

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

    @Column(name = "integration_url")
    private String integrationUrl;

    @Column(name = "created_date")
    private String createdDate;

    public int getOnboardingStepId() {
        return onboardingStepId;
    }

    public void setOnboardingStepId(int onboardingStepId) {
        this.onboardingStepId = onboardingStepId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getOnboardingStep() {
        return onboardingStep;
    }

    public void setOnboardingStep(String onboardingStep) {
        this.onboardingStep = onboardingStep;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getOnboardingStepThreshold() {
        return onboardingStepThreshold;
    }

    public void setOnboardingStepThreshold(String onboardingStepThreshold) {
        this.onboardingStepThreshold = onboardingStepThreshold;
    }

    public String getIntegrationUrl() {
        return integrationUrl;
    }

    public void setIntegrationUrl(String integrationUrl) {
        this.integrationUrl = integrationUrl;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
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
        return "MapMethodOnboardingStep [onboardingStepId=" + onboardingStepId + ", methodName=" + methodName
                + ", templateId=" + templateId + ", onboardingStep=" + onboardingStep + ", sequence=" + sequence
                + ", onboardingStepThreshold=" + onboardingStepThreshold + ", andriodTFliteThreshold="
                + andriodTFliteThreshold + ", andriodDTTThreshold=" + andriodDTTThreshold + ", iosTFliteThreshold="
                + iosTFliteThreshold + ", iosDTTThreshold=" + iosDTTThreshold + ", integrationUrl=" + integrationUrl
                + ", createdDate=" + createdDate + "]";
    }

}
