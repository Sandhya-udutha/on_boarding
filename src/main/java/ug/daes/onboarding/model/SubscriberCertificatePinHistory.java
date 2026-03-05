package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;


@Entity
@Table(name = "subscriber_certificate_pin_history")
@NamedQuery(name = "SubscriberCertificatePinHistory.findAll", query = "SELECT s FROM SubscriberCertificatePinHistory s")
public class SubscriberCertificatePinHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "auth_pin_list")
    private String authPinList;


    @Column(name = "sign_pin_list")
    private String signPinList;

    @Column(name = "subscriber_certificate_pin_history_id")
    private int subscriberCertificatePinHistoryId;

    @Id
    @Column(name = "subscriber_uid", nullable = false, unique = true)
    private String subscriberUid;


    /**
     * Default no-argument constructor.
     * <p>
     * Required for frameworks like Jackson for deserialization.
     * </p>
     */

    public SubscriberCertificatePinHistory() {
        // Intentionally empty: required for Jackson deserialization
    }

    public String getAuthPinList() {
        return this.authPinList;
    }

    public void setAuthPinList(String authPinList) {
        this.authPinList = authPinList;
    }

    public String getSignPinList() {
        return this.signPinList;
    }

    public void setSignPinList(String signPinList) {
        this.signPinList = signPinList;
    }

    public int getSubscriberCertificatePinHistoryId() {
        return this.subscriberCertificatePinHistoryId;
    }

    public void setSubscriberCertificatePinHistoryId(int subscriberCertificatePinHistoryId) {
        this.subscriberCertificatePinHistoryId = subscriberCertificatePinHistoryId;
    }

    public String getSubscriberUid() {
        return this.subscriberUid;
    }

    public void setSubscriberUid(String subscriberUid) {
        this.subscriberUid = subscriberUid;
    }

}