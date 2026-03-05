package ug.daes.onboarding.dto;

import java.io.Serializable;
import java.util.List;

import ug.daes.onboarding.model.OnboardingSteps;

public class TemplateDTO {


    private int templateId;

    private String templateName;

    private String templateMethod;

    private int sequence;

    private String createdBy;

    private String approvedBy;

    private String updatedBy;

    private String publishedStatus;

    private List<OnboardingSteps> steps;

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<OnboardingSteps> getSteps() {
        return steps;
    }

    public void setSteps(List<OnboardingSteps> steps) {
        this.steps = steps;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getTemplateMethod() {
        return templateMethod;
    }

    public void setTemplateMethod(String templateMethod) {
        this.templateMethod = templateMethod;
    }

    public String getPublishedStatus() {
        return publishedStatus;
    }

    public void setPublishedStatus(String publishedStatus) {
        this.publishedStatus = publishedStatus;
    }

    @Override
    public String toString() {
        return "TemplateDTO [templateId=" + templateId + ", templateName=" + templateName + ", templateMethod="
                + templateMethod + ", sequence=" + sequence + ", createdBy=" + createdBy + ", approvedBy=" + approvedBy
                + ", updatedBy=" + updatedBy + ", publishedStatus=" + publishedStatus + ", steps=" + steps + "]";
    }

}
