package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "subscriber_onboarding_templates")
@NamedQuery(name = "SubscriberOnboardingTemplate.findAll", query = "SELECT s FROM SubscriberOnboardingTemplate s")
public class SubscriberOnboardingTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "published_status")
    private String publishedStatus;

    private String state;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private int templateId;

    @Column(name = "template_method")
    private String templateMethod;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "upated_date")
    private String upatedDate;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * Default no-argument constructor.
     * Required for frameworks like Jackson for deserialization.
     */
    public SubscriberOnboardingTemplate() {
        // Required by Jackson for object deserialization.
        // No initialization logic needed.
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getPublishedStatus() {
        return this.publishedStatus;
    }

    public void setPublishedStatus(String publishedStatus) {
        this.publishedStatus = publishedStatus;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getTemplateMethod() {
        return this.templateMethod;
    }

    public void setTemplateMethod(String templateMethod) {
        this.templateMethod = templateMethod;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getUpatedDate() {
        return this.upatedDate;
    }

    public void setUpatedDate(String upatedDate) {
        this.upatedDate = upatedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    @Override
    public String toString() {
        return "SubscriberOnboardingTemplate [createdDate=" + createdDate + ", publishedStatus=" + publishedStatus
                + ", state=" + state + ", templateId=" + templateId + ", templateMethod=" + templateMethod
                + ", templateName=" + templateName + ", upatedDate=" + upatedDate + ", remarks=" + remarks
                + ", createdBy=" + createdBy + ", approvedBy=" + approvedBy + ", updatedBy=" + updatedBy + "]";
    }

}