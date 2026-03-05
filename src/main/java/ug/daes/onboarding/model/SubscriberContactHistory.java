package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "subscriber_contact_history")
@NamedQuery(name = "SubscriberContactHistory.findAll", query = "SELECT s FROM SubscriberContactHistory s")
public class SubscriberContactHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "subscriber_contact_history_id")
    private int subscriberContactHistoryId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "mobile_number")
    private String mobileNumber;


    @Column(name = "subscriber_uid")
    private String subscriberUid;

    /**
     * Default no-argument constructor.
     * <p>
     * Required for frameworks like Jackson for deserialization.
     * </p>
     */

    public SubscriberContactHistory() {
        // Intentionally empty: required for Jackson deserialization
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getSubscriberContactHistoryId() {
        return this.subscriberContactHistoryId;
    }

    public void setSubscriberContactHistoryId(int subscriberContactHistoryId) {
        this.subscriberContactHistoryId = subscriberContactHistoryId;
    }

    public String getSubscriberUid() {
        return this.subscriberUid;
    }

    public void setSubscriberUid(String subscriberUid) {
        this.subscriberUid = subscriberUid;
    }

    @Override
    public String toString() {
        return "SubscriberContactHistory{" +
                "subscriberContactHistoryId=" + subscriberContactHistoryId +
                ", createdDate=" + createdDate +
                ", emailId='" + emailId + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", subscriberUid='" + subscriberUid + '\'' +
                '}';
    }
}