package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "subscriber_complete_details")
@NamedQuery(name = "SubscriberCompleteDetail.findAll", query = "SELECT s FROM SubscriberCompleteDetail s")
public class SubscriberCompleteDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Temporal(TemporalType.TIMESTAMP)


    @Column(name = "cerificate_expiry_date")
    private Date cerificateExpiryDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "certificate_issue_date")
    private Date certificateIssueDate;

    @Column(name = "certificate_status")
    private String certificateStatus;

    @Column(name = "certificate_serial_number")
    private String certificateSerialNumber;

    @Column(name = "full_name")
    private String fullName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "device_status")
    private String deviceStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "device_registration_time")
    private Date deviceRegistrationTimne;

    @Column(name = "id_doc_number")
    private String idDocNumber;

    @Column(name = "id_doc_type")
    private String idDocType;

    @Column(name = "subscriber_status")
    private String subscriberStatus;

    @Id
    @Column(name = "subscriber_uid")
    private String subscriberUid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sign_pin_set_date")
    private Date signPinSetDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "auth_pin_set_date")
    private Date authPinSetDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "on_boarding_time")
    private Date onBoardingTime;

    @Column(name = "selfie_uri")
    private String selfieUri;

    @Column(name = "on_boarding_method")
    private String onBoardingMethod;

    @Column(name = "level_of_assurance")
    private String levelOfAssurance;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "geo_location")
    private String geoLocation;

    @Column(name = "gender")
    private String gender;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "revocation_date")
    private Date revocationDate;

    @Column(name = "revocation_reason")
    private String revocationReason;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "video_type")
    private String videoType;


    /**
     * Default no-argument constructor.
     * <p>
     * Required for frameworks like Jackson for deserialization.
     * </p>
     */

    public SubscriberCompleteDetail() {
        // Intentionally empty: required for Jackson deserialization
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }


    public Date getCerificateExpiryDate() {
        return this.cerificateExpiryDate;
    }

    public void setCerificateExpiryDate(Date cerificateExpiryDate) {
        this.cerificateExpiryDate = cerificateExpiryDate;
    }

    public Date getCertificateIssueDate() {
        return this.certificateIssueDate;
    }

    public void setCertificateIssueDate(Date certificateIssueDate) {
        this.certificateIssueDate = certificateIssueDate;
    }

    public String getCertificateStatus() {
        return this.certificateStatus;
    }

    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDeviceStatus() {
        return this.deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Date getDeviceRegistrationTimne() {
        return this.deviceRegistrationTimne;
    }

    public void setDeviceRegistrationTimne(Date deviceRegistrationTimne) {
        this.deviceRegistrationTimne = deviceRegistrationTimne;
    }

    public String getIdDocNumber() {
        return this.idDocNumber;
    }

    public void setIdDocNumber(String idDocNumber) {
        this.idDocNumber = idDocNumber;
    }

    public String getIdDocType() {
        return this.idDocType;
    }

    public void setIdDocType(String idDocType) {
        this.idDocType = idDocType;
    }


    public String getSubscriberStatus() {
        return this.subscriberStatus;
    }

    public void setSubscriberStatus(String subscriberStatus) {
        this.subscriberStatus = subscriberStatus;
    }

    public String getSubscriberUid() {
        return this.subscriberUid;
    }

    public void setSubscriberUid(String subscriberUid) {
        this.subscriberUid = subscriberUid;
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


}