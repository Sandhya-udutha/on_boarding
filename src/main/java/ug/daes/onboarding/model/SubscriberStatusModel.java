package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;


@Entity
@Table(name = "subscriber_status")
@NamedQuery(name = "SubscriberStatus.findAll", query = "SELECT s FROM SubscriberStatus s")
public class SubscriberStatusModel implements Serializable {
    private static final long serialVersionUID = 1L;


    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "otp_verified_status")
    private String otpVerifiedStatus;

    @Column(name = "subscriber_status")
    private String subscriberStatus;

    @Column(name = "subscriber_status_description")
    private String subscriberStatusDescription;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscriber_status_id")
    private int subscriberStatusId;

    @Column(name = "subscriber_uid")
    private String subscriberUid;


    @Column(name = "updated_date")
    private String updatedDate;


    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getOtpVerifiedStatus() {
        return this.otpVerifiedStatus;
    }

    public void setOtpVerifiedStatus(String otpVerifiedStatus) {
        this.otpVerifiedStatus = otpVerifiedStatus;
    }

    public String getSubscriberStatus() {
        return this.subscriberStatus;
    }

    public void setSubscriberStatus(String subscriberStatus) {
        this.subscriberStatus = subscriberStatus;
    }

    public String getSubscriberStatusDescription() {
        return this.subscriberStatusDescription;
    }

    public void setSubscriberStatusDescription(String subscriberStatusDescription) {
        this.subscriberStatusDescription = subscriberStatusDescription;
    }

    public int getSubscriberStatusId() {
        return this.subscriberStatusId;
    }

    public void setSubscriberStatusId(int subscriberStatusId) {
        this.subscriberStatusId = subscriberStatusId;
    }

    public String getSubscriberUid() {
        return this.subscriberUid;
    }

    public void setSubscriberUid(String subscriberUid) {
        this.subscriberUid = subscriberUid;
    }

    public String getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

}