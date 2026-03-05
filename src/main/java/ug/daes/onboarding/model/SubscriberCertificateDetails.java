package ug.daes.onboarding.model;

import java.io.Serializable;


import jakarta.persistence.*;


@Entity
@Table(name = "subscriber_certificates_details")
@NamedQuery(name = "SubscriberCertificateDetails.findAll", query = "SELECT s FROM SubscriberCertificateDetails s")
public class SubscriberCertificateDetails implements Serializable {

    private static final long serialVersionUID = 1L;


    @Column(name = "subscriber_uid")
    private String subscriberUid;

    @Column(name = "id_doc_number")
    private String idDocNumber;

    @Column(name = "id_doc_type")
    private String idDocType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "cerificate_expiry_date")
    private String cerificateExpiryDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "certificate_issue_date")
    private String certificateIssueDate;

    @Column(name = "certificate_status")
    private String certificateStatus;

    @Column(name = "certificate_type")
    private String certificateType;

    @Id
    @Column(name = "certificate_serial_number")
    private String certificateSerialNumber;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "on_boarding_method")
    private String onboardingMethod;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private String createdDate;

    public String getSubscriberUid() {
        return subscriberUid;
    }

    public void setSubscriberUid(String subscriberUid) {
        this.subscriberUid = subscriberUid;
    }

    public String getIdDocNumber() {
        return idDocNumber;
    }

    public void setIdDocNumber(String idDocNumber) {
        this.idDocNumber = idDocNumber;
    }

    public String getIdDocType() {
        return idDocType;
    }

    public void setIdDocType(String idDocType) {
        this.idDocType = idDocType;
    }

    public String getCerificateExpiryDate() {
        return cerificateExpiryDate;
    }

    public void setCerificateExpiryDate(String cerificateExpiryDate) {
        this.cerificateExpiryDate = cerificateExpiryDate;
    }

    public String getCertificateIssueDate() {
        return certificateIssueDate;
    }

    public void setCertificateIssueDate(String certificateIssueDate) {
        this.certificateIssueDate = certificateIssueDate;
    }

    public String getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateSerialNumber() {
        return certificateSerialNumber;
    }

    public void setCertificateSerialNumber(String certificateSerialNumber) {
        this.certificateSerialNumber = certificateSerialNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getOnboardingMethod() {
        return onboardingMethod;
    }

    public void setOnboardingMethod(String onboardingMethod) {
        this.onboardingMethod = onboardingMethod;
    }

    @Override
    public String toString() {
        return "SubscriberCertificateDetails [subscriberUid=" + subscriberUid + ", idDocNumber=" + idDocNumber
                + ", idDocType=" + idDocType + ", cerificateExpiryDate=" + cerificateExpiryDate
                + ", certificateIssueDate=" + certificateIssueDate + ", certificateStatus=" + certificateStatus
                + ", certificateType=" + certificateType + ", certificateSerialNumber=" + certificateSerialNumber
                + ", fullName=" + fullName + ", onboardingMethod=" + onboardingMethod + ", createdDate=" + createdDate
                + "]";
    }

}
