package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "subscriber_certificate_life_cycle")
@NamedQuery(name = "SubscriberCertificateLifeCycle.findAll", query = "SELECT s FROM SubscriberCertificateLifeCycle s")
public class SubscriberCertificateLifeCycle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscriber_certificate_life_cycle_id", length = 11)
    private int subscriberCertificateLifeCycleId;

    @Column(name = "certificate_serial_number")
    private String certificateSerialNumber;

    @Column(name = "certificate_status")
    private String certificateStatus;

    @Column(name = "certificate_type")
    private String certificateType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "revocation_date")
    private Date revocationDate;

    @Column(name = "revocation_reason")
    private String revocationReason;


    @Column(name = "subscriber_uid")
    private String subscriberUid;

    public String getCertificateSerialNumber() {
        return this.certificateSerialNumber;
    }

    public void setCertificateSerialNumber(String certificateSerialNumber) {
        this.certificateSerialNumber = certificateSerialNumber;
    }

    public String getCertificateStatus() {
        return this.certificateStatus;
    }

    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public String getCertificateType() {
        return this.certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getRevocationDate() {
        return this.revocationDate;
    }

    public void setRevocationDate(Date revocationDate) {
        this.revocationDate = revocationDate;
    }

    public String getRevocationReason() {
        return this.revocationReason;
    }

    public void setRevocationReason(String revocationReason) {
        this.revocationReason = revocationReason;
    }

    public int getSubscriberCertificateLifeCycleId() {
        return this.subscriberCertificateLifeCycleId;
    }

    public void setSubscriberCertificateLifeCycleId(int subscriberCertificateLifeCycleId) {
        this.subscriberCertificateLifeCycleId = subscriberCertificateLifeCycleId;
    }

    public String getSubscriberUid() {
        return this.subscriberUid;
    }

    public void setSubscriberUid(String subscriberUid) {
        this.subscriberUid = subscriberUid;
    }

}